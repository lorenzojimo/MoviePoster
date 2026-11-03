package com.example.poster1019;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The one place that owns the app's movie data: all posters, the selection, and the favorites.
 */
public class PosterRepository {

    // Notified whenever favorites are added or removed (Favorite List, badge).
    public interface FavoritesListener{
        void onFavoritesChanged();
    }

    private static PosterRepository instance;

    private final List<Posters> allPosters;
    private final FavoritesStore favoritesStore;
    private final List<FavoritesListener> favoritesListeners = new ArrayList<>();

    // Creates the single instance on first use. It keeps the application context,
    // never an Activity: holding an Activity in a singleton would leak it.
    public static synchronized PosterRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PosterRepository(context.getApplicationContext());
        }
        return instance;
    }

    private PosterRepository(Context appContext) {
        allPosters = Collections.unmodifiableList(createPosters());
        favoritesStore = new FavoritesStore(appContext);

        // Forget saved favorites whose poster no longer exists in the app.
        List<String> validIds = new ArrayList<>();
        for (Posters poster : allPosters) {
            validIds.add(poster.id);
        }
        favoritesStore.retainOnly(validIds);
    }

    public List<Posters> getAllPosters(){
        return allPosters;
    }

    public Posters findById(String id){
        for(Posters posters:allPosters){
            if(posters.id.equals(id)){
                return posters;
            }
        }
        return null;
    }

    // deselects every poster
    public void clearSelection(){
        for(Posters poster : allPosters){
            poster.isSelected = false;
        }
    }

    //get favorite posters, in the order they were added
    public List<Posters> getFavorites(){
        List<Posters> favorites = new ArrayList<>();
        for (String id : favoritesStore.getIds()) {
            Posters poster = findById(id);
            if (poster != null) {
                favorites.add(poster);
            }
        }
        return favorites;
    }

    public int getFavoriteCount(){
        return favoritesStore.getIds().size();
    }

    // Adds posters to favorites, skipping ones already there.
    // Returns how many were new, so the page can show the right message.
    public int addFavorites(List<Posters> posters) {
        List<String> ids = new ArrayList<>();
        for (Posters poster : posters) {
            ids.add(poster.id);
        }
        int added = favoritesStore.addAll(ids);
        if (added > 0) {
            notifyFavoritesChanged();
        }
        return added;
    }

    // Removes one favorite. Returns true if it was a favorite.
    public boolean removeFavorite(String id) {
        boolean removed = favoritesStore.remove(id);
        if (removed) {
            notifyFavoritesChanged();
        }
        return removed;
    }

    // The listener (observer) pattern: pages register to hear about changes.
    // Whoever adds a listener must remove it again (onDestroyView / onDestroy).
    public void addFavoritesListener(FavoritesListener listener) {
        if (!favoritesListeners.contains(listener)) {
            favoritesListeners.add(listener);
        }
    }

    public void removeFavoritesListener(FavoritesListener listener) {
        favoritesListeners.remove(listener);
    }

    private void notifyFavoritesChanged() {
        for(FavoritesListener listener : new ArrayList<>(favoritesListeners)){
            listener.onFavoritesChanged();
        }
    }

    private static List<Posters> createPosters() {
        List<Posters> posters = new ArrayList<>();

        Posters crimeMovie = new Posters();
        crimeMovie.id = "scarface";
        crimeMovie.image = R.drawable.poster_scarface;
        crimeMovie.name = "Scarface";
        crimeMovie.rating = 5f;
        crimeMovie.createdBy = "Brian De Palma";
        crimeMovie.story = "A Cuban immigrant rises through the Miami drug trade in the 1980s.";
        posters.add(crimeMovie);

        Posters funnyMovie = new Posters();
        funnyMovie.id = "whitechicks";
        funnyMovie.image = R.drawable.poster_whitechicks;
        funnyMovie.name = "White Chicks";
        funnyMovie.rating = 4f;
        funnyMovie.createdBy = "Keenen Ivory Wayans";
        funnyMovie.story = "Two FBI agents go undercover as wealthy socialites to stop a kidnapping.";
        posters.add(funnyMovie);

        Posters actionMovie = new Posters();
        actionMovie.id = "whitehousedown";
        actionMovie.image = R.drawable.poster_whitehousedown;
        actionMovie.name = "White House Down";
        actionMovie.rating = 4f;
        actionMovie.createdBy = "Roland Emmerich";
        actionMovie.story = "A police officer must protect the president when armed attackers take over the White House.";
        posters.add(actionMovie);

        Posters vampireMovie = new Posters();
        vampireMovie.id = "underworld";
        vampireMovie.image = R.drawable.poster_underworld;
        vampireMovie.name = "Underworld: Blood Wars";
        vampireMovie.rating = 4f;
        vampireMovie.createdBy = "Anna Foerster";
        vampireMovie.story = "A vampire warrior fights to survive as her clan and the werewolves are at war.";
        posters.add(vampireMovie);

        Posters violenceMovie = new Posters();
        violenceMovie.id = "equalizer";
        violenceMovie.image = R.drawable.poster_equalizer;
        violenceMovie.name = "The Equalizer";
        violenceMovie.rating = 5f;
        violenceMovie.createdBy = "Antoine Fuqua";
        violenceMovie.story = "A quiet retired agent steps in to protect people who can't protect themselves.";
        posters.add(violenceMovie);

        return posters;

    }


}
