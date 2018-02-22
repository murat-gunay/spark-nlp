package com.johnsnowlabs.ml.lstm

import com.johnsnowlabs.ml.common.EvaluationMetrics
import com.johnsnowlabs.ml.logreg.{I2b2DatasetReader}
import com.johnsnowlabs.nlp.annotators.assertion.logreg.{SimpleTokenizer, Tokenizer, Windowing}
import com.johnsnowlabs.nlp.annotators.datasets.I2b2AnnotationAndText
import com.johnsnowlabs.nlp.embeddings.WordEmbeddings
import org.apache.spark.sql.functions._
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator

object I2b2DatasetLSTMTest extends App with EvaluationMetrics {

  val tokenizer: Tokenizer = new SimpleTokenizer
  lazy val wordVectors: Option[WordEmbeddings] = reader.wordVectors

  println(System.getenv().get("PATH"))

  val extraFeatSize: Int = 10
  val nonTargetMark = normalize(Array.fill(extraFeatSize)(0.1f))
  val targetMark = normalize(Array.fill(extraFeatSize)(-0.1f))


  val mappings = Map("hypothetical" -> 0,
    "present" -> 1, "absent" -> 2, "possible" -> 3,
    "conditional"-> 4, "associated_with_someone_else" -> 5)

  // directory of the i2b2 dataset
  val i2b2Dir = "/home/ubuntu/i2b2"

  val trainDatasetPath = Seq(s"${i2b2Dir}/concept_assertion_relation_training_data/partners",
    s"${i2b2Dir}/concept_assertion_relation_training_data/beth")

  val testDatasetPath = Seq(s"$i2b2Dir/test_data")

  val embeddingsDims = 200
  // word embeddings location
  val embeddingsFile = s"/home/ubuntu/PubMed-shuffle-win-2.bin"
  //val embeddingsFile = s"/home/jose/wordembeddings/pubmed_i2b2.bin"

  val reader = new I2b2DatasetReader(wordEmbeddingsFile = embeddingsFile, targetLengthLimit = 8)
  val trainAnnotations = reader.read(trainDatasetPath)
  val trainDataset = extractFeatures(trainAnnotations).toArray
  val trainLabels = extractLabels(trainAnnotations)
  val trainDatasetIterator = new LSTMRecordIterator(trainDataset, trainLabels.toArray)

  println("trainDsSize: " +  trainDataset.size)


  val lranges:List[Double] = (3.5e-7 to 3.5e-7).by(5e-8).toList
  val innerLayerSize:List[Int] = List(30, 32)

  val testAnnotations = reader.read(testDatasetPath)
  val testDataset = extractFeatures(testAnnotations).toArray
  val testLabels = extractLabels(testAnnotations)
  val testDatasetIterator = new LSTMRecordIterator(testDataset, testLabels.toArray)


  for (lambda <- lranges; ils <-innerLayerSize) {

    trainDatasetIterator.reset
    testDatasetIterator.reset

    val model = train(trainDatasetIterator, trainLabels.toArray, lambda, ils)
    println("testDsSize: " + testDataset.size)

    // Compute raw scores on the test set.
    import scala.collection.JavaConversions._
    val pred: Array[Int] = testDatasetIterator.map { t =>
      val features = t.getFeatureMatrix()
      val inMask = t.getFeaturesMaskArray()
      val outMask = t.getLabelsMaskArray()
      /* predicted probabilities for this batch */
      val predicted = model.output(features, false, inMask, outMask)
      (0 to predicted.size(0) - 1).map {idx =>
        predict(outMask.getRow(idx), predicted.getRow(idx))
      }.toArray
    }.toArray.flatten

    val gold = testLabels

    println(calcStat(pred, gold))
    println(confusionMatrix(pred, gold))
  }

  /* predict based on a single output mask */
  private def predict(outMask: INDArray, predicted:INDArray): Int = {
    var maxScore = -1.0
    var maxIndex = 0
    val outIdx = getIndexOfOne(outMask)
    (0 to predicted.size(0) - 1) foreach { classn =>
      if (predicted.getDouble(classn, outIdx) > maxScore){
        maxScore = predicted.getDouble(classn, outIdx)
        maxIndex = classn
      }
    }
    maxIndex
  }

  private def getIndexOfOne(row : INDArray):Int = {
    var idx = 0
    (0 to row.size(1) - 1).foreach { i =>
      if (row.data().getDouble(i) == 1.0)
        idx = i
    }
    idx
  }


  def train(dataset: DataSetIterator, labels: Array[Int], lambda:Double, ils:Int) = {
    println(lambda, ils)
    val biLSTM = new BiLSTM(lambda, ils)
    (1 to 13).foreach {i =>
       println(s"epoch: $i")
       biLSTM.model.fit(dataset)
    }
    biLSTM.model

  }

  def labelToNumber = udf { label:String  => mappings.get(label)}

  /* transform annotations into arrays of embeddings */
  def extractFeatures(annotations: Seq[I2b2AnnotationAndText]) = annotations.map { annotation =>
    val textTokens = annotation.text.split(" ")
    val leftC = textTokens.slice(0, annotation.start).map(wordVectors.get.getEmbeddings).map(normalize).map(_ ++ nonTargetMark)
    val rightC = textTokens.slice(annotation.end + 1, textTokens.length).map(wordVectors.get.getEmbeddings).map(normalize).map(_ ++ nonTargetMark)
    val target = textTokens.slice(annotation.start, annotation.end + 1).map(wordVectors.get.getEmbeddings).map(normalize).map(_ ++ targetMark)
    val ttokens = textTokens.slice(annotation.start, annotation.end + 1)
    leftC ++ target ++ rightC
  }

  def extractLabels(annotations: Seq[I2b2AnnotationAndText]) = annotations.map{_.label}.map{label => mappings.get(label).get}

  def l2norm(xs: Array[Float]):Float = {
    import math._
    sqrt(xs.map{ x => pow(x, 2)}.sum).toFloat
  }

  def normalize(vec: Array[Float]) : Array[Float] = {
    val norm = l2norm(vec) + 1.0f
    vec.map(element => element / norm)
  }

}
