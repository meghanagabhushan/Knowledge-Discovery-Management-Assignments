package openie;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Quadruple;
import rita.RiWordNet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Megha Nagabhushan on 7/13/2017.
 */
public class CoreClass {

    public static String returnTriplets(String sentence) throws IOException {

        Document doc = new Document(sentence);
       // Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tripletss.txt"), "utf-8"));
        String filename= "triplets.txt";
        String subjectClassFile = "subjectClassFile.txt";
        FileWriter fw = new FileWriter(filename,true);
        FileWriter subjectClass = new FileWriter(subjectClassFile,true);
        RiWordNet wordnet = new RiWordNet("C:\\Users\\Megha Nagabhushan\\Documents\\KDM\\WordNet-3.0.tar\\WordNet-3.0");
        HashMap<String,String[]> predicateSynonyms = new HashMap<>();
        HashMap<String,String[]> subjectSynonyms = new HashMap<>();
        HashMap<String,String[]> objectSynonyms = new HashMap<>();
        HashMap<String,String[]> subjectParent = new HashMap<>();
        HashMap<String,String[]> objectParent = new HashMap<>();
        ArrayList<String> subjectList = new ArrayList<>();


        String triplet="";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
             Collection<Quadruple<String, String, String, Double>> l=sent.openie();
            for(Quadruple x : l){

                String subject = (String) x.first();
                subjectList.add(subject);
                String subNER = returnNER(subject);
                String predicate  = (String) x.second();
                String object = (String) x.third();
                String objNER = returnNER(object);
                String[] synonyms = new String[100];
                String[] synonyms2 = new String[100];
                String[] synonyms3 = new String[100];
                String[] parent = new String[100];
                String[] parent2 = new String[100];
                //int index = sent.sentenceIndex();
                triplet = subject+predicate+object;
                String[] posPredicate = wordnet.getPos(predicate);
                System.out.println("predicate Word : "+predicate);
                for (int j = 0; j < posPredicate.length; j++) {
                    synonyms = wordnet.getAllSynonyms(predicate,posPredicate[j],10);
                    for (int i = 0; i < synonyms.length; i++){
                        System.out.println(synonyms[i]);
                    }
                }
                predicateSynonyms.put(predicate,synonyms);
                System.out.println(predicateSynonyms);


                String[] posSubject = wordnet.getPos(subject);
                String bestPosSubject = wordnet.getBestPos(subject);
                for (int j = 0; j < posSubject.length; j++) {
                    synonyms2 = wordnet.getAllSynonyms(subject,posSubject[j],10);
                    parent = wordnet.getCommonParents(subject,subject,bestPosSubject);
                    System.out.println("parent of"+subject);
                    for (int i = 0; i < parent.length; i++){
                        System.out.println(parent[i]);
                    }
                }
                subjectSynonyms.put(subject,synonyms2);
                subjectParent.put(subject,parent);

                String[] posObject = wordnet.getPos(object);
                String bestPosObject = wordnet.getBestPos(object);
                for (int j = 0; j < posObject.length; j++) {
                    synonyms3 = wordnet.getAllSynonyms(object,posObject[j],10);
                    parent2 = wordnet.getCommonParents(object,object,bestPosObject);
                    System.out.println("parent of"+object);
                    for (int i = 0; i < parent.length; i++){
                        System.out.println(parent[i]);
                    }
                }
                objectSynonyms.put(object,synonyms3);
                objectParent.put(object,parent2);




                //writer.write("Triplet:"+subject+","+predicate+","+object+"\t\tSubject: "+subject+"\t"+"NER: "+subNER+"\t\t"+"object: "+object+"\tNER: "+objNER+"\n");
                fw.write("Triplet:"+subject+","+predicate+","+object+"\t\tSubject: "+subject+"\t"+"NER: "+subNER+"\t\t"+"object: "+object+"\tNER: "+objNER+"\n");
            }
            fw.close();
            //writer.close();

        }

        return triplet;
    }
    public static String returnNER(String word){
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(word);
        pipeline.annotate(document);
        String stringNER = "";
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                stringNER = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            }
        }
        return stringNER;
    }
}
