package com.dabomstew.pkrandom.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A set of available pokemon to choose from with convenience methods (such as
 * only legendary or no legendary)
 * 
 * @author Lucas Vander Wal
 *
 */
public class PokemonPool {

    public static PokemonPool build(List<Pokemon> allPokemon, GenRestrictions restrictions) {
        // TODO: are we sure we can filter out null?
        allPokemon = allPokemon.stream().filter(p -> p != null).collect(Collectors.toList());
        if (restrictions == null) {
            return new PokemonPool(allPokemon);
        }
        Set<Pokemon> pokemonToUse = new LinkedHashSet<>(); // preserve insertion order

        if (restrictions.allow_gen1) {
            addPokesFromRange(pokemonToUse, allPokemon, 1, 151);
            if (restrictions.assoc_g1_g2 && allPokemon.size() > 251)
                addEvosFromRange(pokemonToUse, 1, 151, 152, 251);
            if (restrictions.assoc_g1_g4 && allPokemon.size() > 493)
                addEvosFromRange(pokemonToUse, 1, 151, 387, 493);
        }

        if (restrictions.allow_gen2 && allPokemon.size() > 251) {
            addPokesFromRange(pokemonToUse, allPokemon, 152, 251);
            if (restrictions.assoc_g2_g1) {
                addEvosFromRange(pokemonToUse, 152, 251, 1, 151);
            }
            if (restrictions.assoc_g2_g3 && allPokemon.size() > 386) {
                addEvosFromRange(pokemonToUse, 152, 251, 252, 386);
            }
            if (restrictions.assoc_g2_g4 && allPokemon.size() > 493) {
                addEvosFromRange(pokemonToUse, 152, 251, 387, 493);
            }
        }

        if (restrictions.allow_gen3 && allPokemon.size() > 386) {
            addPokesFromRange(pokemonToUse, allPokemon, 252, 386);
            if (restrictions.assoc_g3_g2) {
                addEvosFromRange(pokemonToUse, 252, 386, 152, 251);
            }
            if (restrictions.assoc_g3_g4 && allPokemon.size() > 493) {
                addEvosFromRange(pokemonToUse, 252, 386, 387, 493);
            }
        }

        if (restrictions.allow_gen4 && allPokemon.size() > 493) {
            addPokesFromRange(pokemonToUse, allPokemon, 387, 493);
            if (restrictions.assoc_g4_g1) {
                addEvosFromRange(pokemonToUse, 387, 493, 1, 151);
            }
            if (restrictions.assoc_g4_g2) {
                addEvosFromRange(pokemonToUse, 387, 493, 152, 251);
            }
            if (restrictions.assoc_g4_g3) {
                addEvosFromRange(pokemonToUse, 387, 493, 252, 386);
            }
        }

        if (restrictions.allow_gen5 && allPokemon.size() > 649) {
            addPokesFromRange(pokemonToUse, allPokemon, 494, 649);
        }

        return new PokemonPool(new ArrayList<>(pokemonToUse));
    }

    private static void addPokesFromRange(Set<Pokemon> pokemonPool, List<Pokemon> allPokemon, int range_min, int range_max) {
        for (int i = range_min; i <= range_max; i++)
            pokemonPool.add(allPokemon.get(i));
    }

    private static void addEvosFromRange(Set<Pokemon> pokemonPool, int first_min, int first_max, int second_min,
            int second_max) {
        Set<Pokemon> toAdd = new LinkedHashSet<>(); // preserve order
        for (Pokemon pk : pokemonPool) {
            if (pk.getNumber() < first_min || pk.getNumber() > first_max)
                continue; // outside range
            for (Evolution ev : pk.getEvolutionsFrom()) {
                if (ev.to.getNumber() >= second_min && ev.to.getNumber() <= second_max) {
                    toAdd.add(ev.to);
                }
            }

            for (Evolution ev : pk.getEvolutionsTo()) {
                if (ev.from.getNumber() >= second_min && ev.from.getNumber() <= second_max) {
                    toAdd.add(ev.from);
                }
            }
        }

        pokemonPool.addAll(toAdd);
    }

    private final List<Pokemon> pokemonList;

    private PokemonPool(List<Pokemon> pokemonList) {
        this.pokemonList = Collections.unmodifiableList(pokemonList);
    }

    /**
     * Get all pokemon in this pool
     * 
     * @return
     */
    public List<Pokemon> getPokemon() {
        return pokemonList;
    }

    /**
     * Get the pokemon in this pool that are either legendary or not.
     * 
     * @param legendary
     * @return
     */
    public Stream<Pokemon> getPokemon(boolean legendary) {
        return pokemonList.stream().filter(pk -> pk.isLegendary() == legendary);
    }
    
    /**
     * Get the pokemon in this pool that are of the given type,
     * optionally filtering out legendaries
     * 
     * @param legendary
     * @param type
     * @return
     */
    public Stream<Pokemon> getPokemonOfType(Type type, boolean noLegendaries) {
        Stream<Pokemon> stream = noLegendaries ? getPokemon(false) : getPokemon().stream();
        return stream.filter(pk -> pk.hasType(type));
    }

}
