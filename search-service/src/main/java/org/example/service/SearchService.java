package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.document.Singer;
import org.example.document.Song;
import org.example.model.Identifier;
import org.example.model.SearchObject;
import org.example.model.SpotifyObject;
import org.example.repository.SingerRepository;
import org.example.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
public class SearchService {

    final static double SINGER_TO_SONG_PERCENTAGE = 0.6;
    final static double SONG_TO_SINGER_PERCENTAGE = 0.5;
    final static double SUB_NAME_PERCENTAGE = 0.8;
    @Autowired
    SongRepository songRepository;

    @Autowired
    SingerRepository singerRepository;

    public List<Identifier> getSongIdentifiers(Song song) {
        List<Identifier> identifiers = new ArrayList<>();
        Singer singer = singerRepository.findById(song.getSingerId()).get();
        identifiers.add(new Identifier(singer.getName(), SINGER_TO_SONG_PERCENTAGE));

        identifiers.addAll(this.getNameIdentifiers(song.getName()));

        return identifiers;
    }

    public List<Identifier> getSingerIdentifiers(Singer singer){
        List<Song> singerSongs = songRepository.findBySingerId(singer.getId());
        List<Identifier> identifiers = new ArrayList<>();
        for (Song song: singerSongs)
            identifiers.add(new Identifier(song.getName(), SONG_TO_SINGER_PERCENTAGE));

        identifiers.addAll(this.getNameIdentifiers(singer.getName()));
        return identifiers;
    }

    public List<Identifier> getNameIdentifiers(String name){
        List<Identifier> identifiers = new ArrayList<>(List.of(new Identifier(name, 1)));
        if (name.contains(" ")){
            List<String> words = List.of(name.split(" "));
            for (int i=1; i < words.size() - 1; i ++) { // get the combination of all sizes from one word to the original word
                List<Identifier> identifierList = this.getNamesCombinationsIdentifiers(words, 0, i);
                identifiers.addAll(identifierList);
            }


        }

        return identifiers;

    }

    private List<Identifier> getNamesCombinationsIdentifiers(List<String> words, int startingIndex, int remainingSize){
        if (remainingSize == 0) // combination algorithm
            return List.of(new Identifier("", SUB_NAME_PERCENTAGE));
        List<Identifier> combinations = new ArrayList<>();
        for (int i =startingIndex; i < words.size(); i ++)
            for (Identifier combination : this.getNamesCombinationsIdentifiers(words, i + 1, remainingSize - 1))
                combinations.add(new Identifier(words.get(i) + " " + combination.getValue(), SUB_NAME_PERCENTAGE));
        return combinations;

    }


    private List<SpotifyObject> getAllSpotifyObjects(){
        List<SpotifyObject> list = new ArrayList<>();
        for (Song song: songRepository.findAll())
            list.add(new SpotifyObject(song, this));
        for (Singer singer: singerRepository.findAll())
            list.add(new SpotifyObject(singer, this));
        return list;
    }

    private List<SearchObject> spotifyObjectsToSearchObjects(List<SpotifyObject> spotifyObjects, String searchString){
        List<SearchObject> searchObjects = new ArrayList<>();
        for (SpotifyObject spotifyObject:spotifyObjects)
            searchObjects.add(new SearchObject(spotifyObject.getId(), this.getScore(spotifyObject, searchString)));
        return searchObjects;
    }

    private SpotifyObject findSpotifyObjectById(List<SpotifyObject> spotifyObjects, String id){
        for (SpotifyObject spotifyObject: spotifyObjects)
            if (spotifyObject.getId().equals(id))
                return spotifyObject;
        return null;
    }


    private List<SpotifyObject> searchObjectsToSortedSpotifyObjects(List<SpotifyObject> spotifyObjects, List<SearchObject> sortedSearchObjects) {
        List<SpotifyObject> sortedSpotifyObjects = new ArrayList<>();
        for (SearchObject searchObject: sortedSearchObjects)
            if (searchObject.getScore() > 0.5)
                sortedSpotifyObjects.add(this.findSpotifyObjectById(spotifyObjects, searchObject.getId()));
        return sortedSpotifyObjects;
    }



    private double getScore(SpotifyObject spotifyObject, String searchString){
        List<Double> scores = new ArrayList<>();
        for (Identifier identifier : spotifyObject.getIdentifiers()) {
            double score = 0;

            for (int i = 0; i < Math.min(searchString.length(), identifier.getValue().length()); i++)
                if (searchString.toLowerCase().charAt(i) == identifier.getValue().toLowerCase().charAt(i))
                    score += identifier.getPercentage();
                else
                    score -= identifier.getPercentage();

            scores.add(score);

        }

        return Collections.max(scores)/searchString.length();
    }

    private void replaceElements(List<SearchObject> list, int index1, int index2){
        SearchObject element1 = list.get(index1);
        SearchObject element2 = list.get(index2);
        list.set(index1, element2);
        list.set(index2, element1);
    }

    private void sortSearchList(List<SearchObject> searchObjects){
       int i ,j ;
       final int SIZE = searchObjects.size();
       for (i=0; i < SIZE; i ++)
           for (j =0; j < SIZE - i - 1; j ++)
               if (searchObjects.get(j).getScore() < searchObjects.get(j + 1).getScore())
                   this.replaceElements(searchObjects, j, j + 1);

    }


    private String spotifyObjectsToJson(List<SpotifyObject> spotifyObjects) throws IOException {
        StringBuilder json = new StringBuilder("[");
        int i = 0;
        for (SpotifyObject object: spotifyObjects) {
            if (object.getType().equals(SpotifyObject.Type.singer)) {
                Singer singer = singerRepository.findById(object.getId()).get();
                json.append(singer.getJsonInfo(this));
            }
            else{
                Song song = songRepository.findById(object.getId()).get();
                json.append(song.getJsonInfo(this));
            }

            if (i != spotifyObjects.size() - 1)
                json.append(",");

            i ++;


        }

        json.append("]");
        return json.toString();
    }

    public String getFoundElements(String searchString) throws IOException {
        // Here we first got all the singers and songs and unified them under the spotify object class, then we created search objects using the spotifyObjects ids
        // and the score using a matching algorithm, then we sorted the search objects using the bubble sort algorithm , and we got a new spotify objects sorted list, that we
        // transformed to json

        List<SpotifyObject> spotifyObjects = this.getAllSpotifyObjects();
        List<SearchObject> searchObjects = this.spotifyObjectsToSearchObjects(spotifyObjects, searchString);

        this.sortSearchList(searchObjects);
        List<SpotifyObject> sortedSpotifyObjects = this.searchObjectsToSortedSpotifyObjects(spotifyObjects, searchObjects);
        return this.spotifyObjectsToJson(sortedSpotifyObjects);


    }

    public Singer getSingerById(String singerId){
        return singerRepository.findById(singerId).get();
    }

    public byte[] getImageBytes(String spotifyObjectId) throws IOException {
        String filePath = "/images-datacenter/" + spotifyObjectId + ".png";
        return Files.readAllBytes(Paths.get(filePath));
    }

    public String getSong(String id) throws IOException {
        return songRepository.findById(id).get().getJsonInfo(this);
    }

    public boolean isUsernameOfASinger(String username){
        return !(singerRepository.findByName(username) == null);
    }

}



























