package com.dabomstew.pkrandom.wild;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.pokemon.EncounterSet;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.romhandlers.RomHandler;

public class WildPokemonModifier {

    final RomHandler romHandler;
    final Settings settings;
    final Random random;
    
    public static WildPokemonModifier build(RomHandler romHandler, Settings settings, Random random) {
        switch (settings.getWildPokemonMod()) {
            case RANDOM:
                return new RandomWildPokemonMod(romHandler, settings, random);
            case AREA_MAPPING:
                return new AreaMappingWildPokemonMod(romHandler, settings, random);
            case GLOBAL_MAPPING:
                return new GlobalMappingWildPokemonMod(romHandler, settings, random);
            default:
                return new WildPokemonModifier(romHandler, settings, random);
        }
    }

    protected WildPokemonModifier(RomHandler romHandler, Settings settings, Random random) {
        this.romHandler = romHandler;
        this.settings = settings;
        this.random = random;
    }
    
    public void modify() {
        // no mod
    }
    
    // TODO is there more logic that could be shared at the parent level?

    protected Pokemon pickWildPowerLvlReplacement(List<Pokemon> pokemonPool, Pokemon current, boolean banSamePokemon,
            List<Pokemon> usedUp) {
        // start with within 10% and add 5% either direction till we find
        // something
        int currentBST = current.bstForPowerLevels();
        int minTarget = currentBST - currentBST / 10;
        int maxTarget = currentBST + currentBST / 10;
        List<Pokemon> canPick = new ArrayList<Pokemon>();
        int expandRounds = 0;
        while (canPick.isEmpty() || (canPick.size() < 3 && expandRounds < 3)) {
            for (Pokemon pk : pokemonPool) {
                if (pk.bstForPowerLevels() >= minTarget && pk.bstForPowerLevels() <= maxTarget
                        && (!banSamePokemon || pk != current) && (usedUp == null || !usedUp.contains(pk))
                        && !canPick.contains(pk)) {
                    canPick.add(pk);
                }
            }
            minTarget -= currentBST / 20;
            maxTarget += currentBST / 20;
            expandRounds++;
        }
        return canPick.get(this.random.nextInt(canPick.size()));
    }
    
    /**
     * Return a new list of pokemon, optionally removing legendaries
     * 
     * @param noLegendaries
     * @return
     */
    protected List<Pokemon> listPokemon(boolean noLegendaries) {
        return noLegendaries ?
                romHandler.getPokemonPool().getPokemon(false).collect(Collectors.toList()) :
                new ArrayList<>(romHandler.getPokemonPool().getPokemon());
    }

    protected Set<Pokemon> pokemonInArea(EncounterSet area) {
        return area.encounters.stream().map(e -> e.pokemon)
                .collect(Collectors.toCollection(TreeSet::new));
    }

}
