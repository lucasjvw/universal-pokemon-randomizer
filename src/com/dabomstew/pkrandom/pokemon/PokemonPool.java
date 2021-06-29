package com.dabomstew.pkrandom.pokemon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
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
    
    // upper limit of pokemon per gen
    private static final int G1 = 151, G2 = 251, G3 = 386, G4 = 493, G5 = 649;

    public static PokemonPool build(List<Pokemon> allPokemon, GenRestrictions restrictions) {
        // TODO: are we sure we can filter out null?
        allPokemon = allPokemon.stream().filter(p -> p != null).collect(Collectors.toList());
        if (restrictions == null) {
            return new PokemonPool(allPokemon);
        }
        Set<Pokemon> pokemonToUse = new LinkedHashSet<>(); // preserve insertion order

        if (restrictions.allow_gen1) {
            addPokesFromRange(pokemonToUse, allPokemon, 0, G1);
            if (restrictions.assoc_g1_g2 && allPokemon.size() >= G2)
                addEvosFromRange(pokemonToUse, 0, G1, G1, G2);
            if (restrictions.assoc_g1_g4 && allPokemon.size() >= G4)
                addEvosFromRange(pokemonToUse, 0, G1, G3, G4);
        }

        if (restrictions.allow_gen2 && allPokemon.size() >= G2) {
            addPokesFromRange(pokemonToUse, allPokemon, G1, G2);
            if (restrictions.assoc_g2_g1) {
                addEvosFromRange(pokemonToUse, G1, G2, 0, G1);
            }
            if (restrictions.assoc_g2_g3 && allPokemon.size() >= G3) {
                addEvosFromRange(pokemonToUse, G1, G2, G2, G3);
            }
            if (restrictions.assoc_g2_g4 && allPokemon.size() >= G4) {
                addEvosFromRange(pokemonToUse, G1, G2, G3, G4);
            }
        }

        if (restrictions.allow_gen3 && allPokemon.size() >= G3) {
            addPokesFromRange(pokemonToUse, allPokemon, G2, G3);
            if (restrictions.assoc_g3_g2) {
                addEvosFromRange(pokemonToUse, G2, G3, G1, G2);
            }
            if (restrictions.assoc_g3_g4 && allPokemon.size() >= G4) {
                addEvosFromRange(pokemonToUse, G2, G3, G3, G4);
            }
        }

        if (restrictions.allow_gen4 && allPokemon.size() >= G4) {
            addPokesFromRange(pokemonToUse, allPokemon, G3, G4);
            if (restrictions.assoc_g4_g1) {
                addEvosFromRange(pokemonToUse, G3, G4, 0, G1);
            }
            if (restrictions.assoc_g4_g2) {
                addEvosFromRange(pokemonToUse, G3, G4, G1, G2);
            }
            if (restrictions.assoc_g4_g3) {
                addEvosFromRange(pokemonToUse, G3, G4, G2, G3);
            }
        }

        if (restrictions.allow_gen5 && allPokemon.size() >= G5) {
            addPokesFromRange(pokemonToUse, allPokemon, G4, G5);
        }

        return new PokemonPool(new ArrayList<>(pokemonToUse));
    }

    private static void addPokesFromRange(Set<Pokemon> pokemonPool, List<Pokemon> allPokemon,
            int minInclusive, int maxExclusive) {
        pokemonPool.addAll(allPokemon.subList(minInclusive, maxExclusive));
    }

    /**
     * Add evolutions of pokemon in the first range if they are in the second range.
     * Minimums are inclusive, maximums are exclusive.
     * 
     * @param pokemonPool
     * @param first_min
     * @param first_max
     * @param second_min
     * @param second_max
     */
    private static void addEvosFromRange(Set<Pokemon> pokemonPool, int first_min, int first_max,
            int second_min, int second_max) {
        Set<Pokemon> toAdd = new LinkedHashSet<>(); // preserve order
        // add the new pokemon while checking if there are additional evolutions
        // for example, if we add gen2 and associations,
        // we want to pick up pikachu AND raichu from gen1
        
        pokemonPool.stream()
            .filter(p -> p.getNumber() >= first_min && p.getNumber() < first_max)
            .flatMap(p -> getEvolutionTree(p))
            .filter(p -> p.getNumber() >= second_min && p.getNumber() < second_max)
            .forEach(toAdd::add);
        pokemonPool.addAll(toAdd);
    }
    
    private static final Stream<Pokemon> getEvolutionTree(Pokemon poke) {
        Set<Pokemon> evolutions = new LinkedHashSet<>();
        LinkedList<Pokemon> toCheck = new LinkedList<>();
        toCheck.add(poke);
        
        Pokemon next;
        while ((next = toCheck.poll()) != null) {
            next.getEvolutionsFrom().forEach(ev -> {
                if (evolutions.add(ev.to)) {
                    toCheck.add(ev.to);
                }
            });
            next.getEvolutionsTo().forEach(ev -> {
                if (evolutions.add(ev.from)) {
                    toCheck.add(ev.from);
                }
            });
        }
        return evolutions.stream();
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
