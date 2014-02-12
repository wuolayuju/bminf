bminf
=====
Para Ejercicio 3:

for (String term: index.getTerms()){
    for (Posting post: index.getTermPostings(term){
        freqTerm += post.getTermFrequency();
    }
    numDocsTerm = index.getTermPostings().size();
}
