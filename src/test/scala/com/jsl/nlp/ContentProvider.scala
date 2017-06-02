package com.jsl.nlp

/**
  * Created by saif on 02/05/17.
  */
object ContentProvider {
  val latinBody: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et " +
    "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
    "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
    "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui " +
    "officia deserunt mollit anim id est laborum."

  val englishPhrase: String = "In the third category he included those Brothers (the majority)" +
    "who saw nothing in Freemasonry but the external forms and ceremonies, and prized the strict" +
    "performance of these forms without troubling about their purport or significance."

  val sbdTestParagraph: String = "In the third category he included those Brothers (the majority) who saw nothing " +
    "in Freemasonry but the external forms and ceremonies, and prized the strict performance of these forms without " +
    "troubling about their purport or significance. Such were Willarski and even the Grand Master of the principal " +
    "lodge. Finally, to the fourth category also a great many Brothers belonged, particularly those who had lately " +
    "joined. These according to Pierre's observations were men who had no belief in anything, nor desire for anything, " +
    "but joined the Freemasons merely to associate with the wealthy young Brothers who were influential through their" +
    " connections or rank, and of whom there were very many in the lodge.\nPierre began to feel dissatisfied with what" +
    " he was doing. Freemasonry, at any rate as he saw it here, sometimes seemed to him based merely on externals." +
    "\nHe did not think of doubting Freemasonry itself, but suspected that Russian Masonry had taken a wrong path" +
    " and deviated from its original principles. And so toward the end of the year he went abroad to be initiated" +
    " into the higher secrets of the order.What is to be done in these circumstances? To favor revolutions, overthrow" +
    " everything, repel force by force?No! We are very far from that. Every violent reform deserves censure, for it" +
    " quite fails to remedy evil while men remain what they are, and also because wisdom needs no violence. \"But" +
    " what is there in running across it like that?\" said Ilagin's groom. \"Once she had missed it and turned it" +
    " away, any mongrel could take it,\" Ilagin was saying at the same time, breathless from his gallop and his" +
    " excitement."

  val wsjTrainingCorpus: String = "Pierre|NNP Vinken|NNP ,|, 61|CD years|NNS old|JJ ,|, will|MD " +
    "join|VB the|DT board|NN as|IN a|DT nonexecutive|JJ director|NN " +
    "Nov.|NNP 29|CD .|.\nMr.|NNP Vinken|NNP is|VBZ chairman|NN of|IN " +
    "Elsevier|NNP N.V.|NNP ,|, the|DT Dutch|NNP publishing|VBG " +
    "group|NN .|. Rudolph|NNP Agnew|NNP ,|, 55|CD years|NNS old|JJ " +
    "and|CC former|JJ chairman|NN of|IN Consolidated|NNP Gold|NNP " +
    "Fields|NNP PLC|NNP ,|, was|VBD named|VBN a|DT nonexecutive|JJ " +
    "director|NN of|IN this|DT British|JJ industrial|JJ conglomerate|NN " +
    ".|.\nA|DT form|NN of|IN asbestos|NN once|RB used|VBN to|TO make|VB " +
    "Kent|NNP cigarette|NN filters|NNS has|VBZ caused|VBN a|DT high|JJ " +
    "percentage|NN of|IN cancer|NN deaths|NNS among|IN a|DT group|NN " +
    "of|IN workers|NNS exposed|VBN to|TO it|PRP more|RBR than|IN " +
    "30|CD years|NNS ago|IN ,|, researchers|NNS reported|VBD .|."

  val targetSentencesFromWsj = Array("A form of asbestos once used to make " +
    "Kent cigarette filters has caused a high percentage of cancer deaths among a group " +
    "of workers exposed to it more than 30 years ago, researchers reported")

}
