package com.dabomstew.pkrandom.wild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.Settings.WildPokemonRestrictionMod;
import com.dabomstew.pkrandom.exceptions.RandomizationException;
import com.dabomstew.pkrandom.pokemon.Encounter;
import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.pokemon.Type;
import com.dabomstew.pkrandom.romhandlers.RomHandler;

class RandomWildPokemonMod extends WildPokemonModifier {

    RandomWildPokemonMod(RomHandler romHandler, Settings settings, Random random) {
        super(romHandler, settings, random);
    }
    
    @Override
    public void modify() {
        boolean useTimeOfDay = settings.isUseTimeBasedEncounters();
        boolean noLegendaries = settings.isBlockWildLegendaries();
        List<EncounterSet> currentEncounters = romHandler.getEncounters(useTimeOfDay);

        // New: randomize the order encounter sets are randomized in.
        // Leads to less predictable results for various modifiers.
        // Need to keep the original ordering around for saving though.
        List<EncounterSet> scrambledEncounters = new ArrayList<>(currentEncounters);
        Collections.shuffle(scrambledEncounters, romHandler.getRandom());

        List<Pokemon> banned = romHandler.bannedForWildEncounters();
        // Assume EITHER catch em all OR type themed OR match strength for now
        if (settings.getWildPokemonRestrictionMod() == WildPokemonRestrictionMod.CATCH_EM_ALL) {

            List<Pokemon> allPokes = listPokemon(noLegendaries);
            allPokes.removeAll(banned);
            for (EncounterSet area : scrambledEncounters) {
                List<Pokemon> pickablePokemon = allPokes;
                if (area.bannedPokemon.size() > 0) {
                    pickablePokemon = new ArrayList<>(allPokes);
                    pickablePokemon.removeAll(area.bannedPokemon);
                }
                for (Encounter enc : area.encounters) {
                    // Pick a random pokemon
                    if (pickablePokemon.isEmpty()) {
                        // Only banned pokes are left, ignore them and pick
                        // something else for now.
                        List<Pokemon> tempPickable = listPokemon(noLegendaries);
                        tempPickable.removeAll(banned);
                        tempPickable.removeAll(area.bannedPokemon);
                        if (tempPickable.isEmpty()) {
                            throw new RandomizationException("ERROR: Couldn't replace a wild Pokemon!");
                        }
                        int picked = this.random.nextInt(tempPickable.size());
                        enc.pokemon = tempPickable.get(picked);
                    } else {
                        // Picked this Pokemon, remove it
                        int picked = this.random.nextInt(pickablePokemon.size());
                        enc.pokemon = pickablePokemon.get(picked);
                        pickablePokemon.remove(picked);
                        if (allPokes != pickablePokemon) {
                            allPokes.remove(enc.pokemon);
                        }
                        if (allPokes.isEmpty()) {
                            // Start again
                            allPokes = listPokemon(noLegendaries);
                            allPokes.removeAll(banned);
                            if (area.bannedPokemon.size() > 0) {
                                pickablePokemon = new ArrayList<>(allPokes);
                                pickablePokemon.removeAll(area.bannedPokemon);
                            }
                        }
                    }
                }
            }
        } else if (settings.getWildPokemonRestrictionMod() == WildPokemonRestrictionMod.TYPE_THEME_AREAS) {
            Map<Type, List<Pokemon>> cachedPokeLists = new TreeMap<Type, List<Pokemon>>();
            for (EncounterSet area : scrambledEncounters) {
                List<Pokemon> possiblePokemon = null;
                int iterLoops = 0;
                while (possiblePokemon == null && iterLoops < 10000) {
                    Type areaTheme = romHandler.randomType();
                    if (!cachedPokeLists.containsKey(areaTheme)) {
                        List<Pokemon> pType = romHandler.getPokemonPool()
                                .getPokemonOfType(areaTheme, noLegendaries).collect(Collectors.toList());
                        pType.removeAll(banned);
                        cachedPokeLists.put(areaTheme, pType);
                    }
                    possiblePokemon = cachedPokeLists.get(areaTheme);
                    if (area.bannedPokemon.size() > 0) {
                        possiblePokemon = new ArrayList<>(possiblePokemon);
                        possiblePokemon.removeAll(area.bannedPokemon);
                    }
                    if (possiblePokemon.size() == 0) {
                        // Can't use this type for this area
                        possiblePokemon = null;
                    }
                    iterLoops++;
                }
                if (possiblePokemon == null) {
                    throw new RandomizationException("Could not randomize an area in a reasonable amount of attempts.");
                }
                for (Encounter enc : area.encounters) {
                    // Pick a random themed pokemon
                    enc.pokemon = possiblePokemon.get(this.random.nextInt(possiblePokemon.size()));
                }
            }
        } else if (settings.getWildPokemonRestrictionMod() == WildPokemonRestrictionMod.SIMILAR_STRENGTH) {
            List<Pokemon> allowedPokes = listPokemon(noLegendaries);
            allowedPokes.removeAll(banned);
            for (EncounterSet area : scrambledEncounters) {
                List<Pokemon> localAllowed = allowedPokes;
                if (area.bannedPokemon.size() > 0) {
                    localAllowed = new ArrayList<>(allowedPokes);
                    localAllowed.removeAll(area.bannedPokemon);
                }
                for (Encounter enc : area.encounters) {
                    enc.pokemon = pickWildPowerLvlReplacement(localAllowed, enc.pokemon, false, null);
                }
            }
        } else {
            // Entirely random
            for (EncounterSet area : scrambledEncounters) {
                for (Encounter enc : area.encounters) {
                    enc.pokemon = noLegendaries ?
                            romHandler.randomNonLegendaryPokemon() : romHandler.randomPokemon();
                    while (banned.contains(enc.pokemon) || area.bannedPokemon.contains(enc.pokemon)) {
                        enc.pokemon = noLegendaries ?
                                romHandler.randomNonLegendaryPokemon() : romHandler.randomPokemon();
                    }
                }
            }
        }

        romHandler.setEncounters(useTimeOfDay, currentEncounters);
    }

}
