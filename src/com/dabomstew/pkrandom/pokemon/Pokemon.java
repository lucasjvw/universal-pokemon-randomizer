package com.dabomstew.pkrandom.pokemon;

/*----------------------------------------------------------------------------*/
/*--  Pokemon.java - represents an individual Pokemon, and contains         --*/
/*--                 common Pokemon-related functions.                      --*/
/*--                                                                        --*/
/*--  Part of "Universal Pokemon Randomizer" by Dabomstew                   --*/
/*--  Pokemon and any associated names and the like are                     --*/
/*--  trademark and (C) Nintendo 1996-2012.                                 --*/
/*--                                                                        --*/
/*--  The custom code written here is licensed under the terms of the GPL:  --*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Pokemon implements Comparable<Pokemon> {

    private static final List<Integer> legendaries = Arrays.asList(144, 145, 146, 150, 151, 243, 244, 245, 249, 250,
            251, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488,
            489, 490, 491, 492, 493, 494, 638, 639, 640, 641, 642, 643, 644, 645, 646, 647, 648, 649);

    private String name;
    private int number;

    private Type primaryType;
    private Type secondaryType;

    private int hp;
    private int attack;
    private int defense;
    private int spatk;
    private int spdef;
    private int speed;
    private int special;

    private int ability1;
    private int ability2;
    private int ability3;

    private int catchRate;
    private int expYield;

    private int guaranteedHeldItem;
    private int commonHeldItem;
    private int rareHeldItem;
    private int darkGrassHeldItem;

    private int genderRatio;

    private int frontSpritePointer;
    private int picDimensions;

    private ExpCurve growthCurve;

    private List<Evolution> evolutionsFrom = new ArrayList<Evolution>();
    private List<Evolution> evolutionsTo = new ArrayList<Evolution>();

    private List<Integer> shuffledStatsOrder;

    public Pokemon() {
        shuffledStatsOrder = Arrays.asList(0, 1, 2, 3, 4, 5);
    }

    public String getName() {
        return name;
    }

    public Pokemon setName(String name) {
        this.name = name;
        return this;
    }

    public int getNumber() {
        return number;
    }

    public Pokemon setNumber(int number) {
        this.number = number;
        return this;
    }

    public Type getPrimaryType() {
        return primaryType;
    }

    public Pokemon setPrimaryType(Type primaryType) {
        this.primaryType = primaryType;
        return this;
    }

    public Type getSecondaryType() {
        return secondaryType;
    }

    public Pokemon setSecondaryType(Type secondaryType) {
        this.secondaryType = secondaryType;
        return this;
    }

    public boolean hasType(Type type) {
        return (primaryType != null && primaryType == type)
                || (secondaryType != null && secondaryType == type);
    }

    public int getHp() {
        return hp;
    }

    public Pokemon setHp(int hp) {
        this.hp = hp;
        return this;
    }

    public int getAttack() {
        return attack;
    }

    public Pokemon setAttack(int attack) {
        this.attack = attack;
        return this;
    }

    public int getDefense() {
        return defense;
    }

    public Pokemon setDefense(int defense) {
        this.defense = defense;
        return this;
    }

    public int getSpatk() {
        return spatk;
    }

    public Pokemon setSpatk(int spatk) {
        this.spatk = spatk;
        return this;
    }

    public int getSpdef() {
        return spdef;
    }

    public Pokemon setSpdef(int spdef) {
        this.spdef = spdef;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public Pokemon setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public int getSpecial() {
        return special;
    }

    public Pokemon setSpecial(int special) {
        this.special = special;
        return this;
    }

    public int getAbility1() {
        return ability1;
    }

    public Pokemon setAbility1(int ability1) {
        this.ability1 = ability1;
        return this;
    }

    public int getAbility2() {
        return ability2;
    }

    public Pokemon setAbility2(int ability2) {
        this.ability2 = ability2;
        return this;
    }

    public int getAbility3() {
        return ability3;
    }

    public Pokemon setAbility3(int ability3) {
        this.ability3 = ability3;
        return this;
    }

    public int getCatchRate() {
        return catchRate;
    }

    public Pokemon setCatchRate(int catchRate) {
        this.catchRate = catchRate;
        return this;
    }

    public int getExpYield() {
        return expYield;
    }

    public Pokemon setExpYield(int expYield) {
        this.expYield = expYield;
        return this;
    }

    public int getGuaranteedHeldItem() {
        return guaranteedHeldItem;
    }

    public Pokemon setGuaranteedHeldItem(int guaranteedHeldItem) {
        this.guaranteedHeldItem = guaranteedHeldItem;
        return this;
    }

    public int getCommonHeldItem() {
        return commonHeldItem;
    }

    public Pokemon setCommonHeldItem(int commonHeldItem) {
        this.commonHeldItem = commonHeldItem;
        return this;
    }

    public int getRareHeldItem() {
        return rareHeldItem;
    }

    public Pokemon setRareHeldItem(int rareHeldItem) {
        this.rareHeldItem = rareHeldItem;
        return this;
    }

    public int getDarkGrassHeldItem() {
        return darkGrassHeldItem;
    }

    public Pokemon setDarkGrassHeldItem(int darkGrassHeldItem) {
        this.darkGrassHeldItem = darkGrassHeldItem;
        return this;
    }

    public int getGenderRatio() {
        return genderRatio;
    }

    public Pokemon setGenderRatio(int genderRatio) {
        this.genderRatio = genderRatio;
        return this;
    }

    public int getFrontSpritePointer() {
        return frontSpritePointer;
    }

    public Pokemon setFrontSpritePointer(int frontSpritePointer) {
        this.frontSpritePointer = frontSpritePointer;
        return this;
    }

    public int getPicDimensions() {
        return picDimensions;
    }

    public Pokemon setPicDimensions(int picDimensions) {
        this.picDimensions = picDimensions;
        return this;
    }

    public ExpCurve getGrowthCurve() {
        return growthCurve;
    }

    public Pokemon setGrowthCurve(ExpCurve growthCurve) {
        this.growthCurve = growthCurve;
        return this;
    }

    public List<Evolution> getEvolutionsFrom() {
        return evolutionsFrom;
    }

    public Pokemon setEvolutionsFrom(List<Evolution> evolutionsFrom) {
        this.evolutionsFrom = evolutionsFrom;
        return this;
    }

    public List<Evolution> getEvolutionsTo() {
        return evolutionsTo;
    }

    public Pokemon setEvolutionsTo(List<Evolution> evolutionsTo) {
        this.evolutionsTo = evolutionsTo;
        return this;
    }

    public List<Integer> getShuffledStatsOrder() {
        return shuffledStatsOrder;
    }

    public Pokemon setShuffledStatsOrder(List<Integer> shuffledStatsOrder) {
        this.shuffledStatsOrder = shuffledStatsOrder;
        return this;
    }

    public void shuffleStats(Random random) {
        Collections.shuffle(shuffledStatsOrder, random);
        applyShuffledOrderToStats();
    }
    
    public void copyShuffledStatsUpEvolution(Pokemon evolvesFrom) {
        setShuffledStatsOrder(evolvesFrom.getShuffledStatsOrder());
        applyShuffledOrderToStats();
    }

    private void applyShuffledOrderToStats() {
        List<Integer> stats = Arrays.asList(getHp(), getAttack(), getDefense(), getSpatk(), getSpdef(), getSpeed());

        // Copy in new stats
        setHp(stats.get(shuffledStatsOrder.get(0)));
        setAttack(stats.get(shuffledStatsOrder.get(1)));
        setDefense(stats.get(shuffledStatsOrder.get(2)));
        setSpatk(stats.get(shuffledStatsOrder.get(3)));
        setSpdef(stats.get(shuffledStatsOrder.get(4)));
        setSpeed(stats.get(shuffledStatsOrder.get(5)));

        // make special the average of spatk and spdef
        setSpecial((int) Math.ceil((getSpatk() + getSpdef()) / 2.0f));
    }

    public void randomizeStatsWithinBST(Random random) {
        if (getNumber() == 292) {
            // Shedinja is horribly broken unless we restrict him to 1HP.
            int bst = bst() - 51;

            // Make weightings
            double atkW = random.nextDouble(), defW = random.nextDouble();
            double spaW = random.nextDouble(), spdW = random.nextDouble(), speW = random.nextDouble();

            double totW = atkW + defW + spaW + spdW + speW;

            setHp(1);
            setAttack((int) Math.max(1, Math.round(atkW / totW * bst)) + 10);
            setDefense((int) Math.max(1, Math.round(defW / totW * bst)) + 10);
            setSpatk((int) Math.max(1, Math.round(spaW / totW * bst)) + 10);
            setSpdef((int) Math.max(1, Math.round(spdW / totW * bst)) + 10);
            setSpeed((int) Math.max(1, Math.round(speW / totW * bst)) + 10);

            // Fix up special too
            setSpecial((int) Math.ceil((getSpatk() + getSpdef()) / 2.0f));

        } else {
            // Minimum 20 HP, 10 everything else
            int bst = bst() - 70;

            // Make weightings
            double hpW = random.nextDouble(), atkW = random.nextDouble(), defW = random.nextDouble();
            double spaW = random.nextDouble(), spdW = random.nextDouble(), speW = random.nextDouble();

            double totW = hpW + atkW + defW + spaW + spdW + speW;

            setHp((int) Math.max(1, Math.round(hpW / totW * bst)) + 20);
            setAttack((int) Math.max(1, Math.round(atkW / totW * bst)) + 10);
            setDefense((int) Math.max(1, Math.round(defW / totW * bst)) + 10);
            setSpatk((int) Math.max(1, Math.round(spaW / totW * bst)) + 10);
            setSpdef((int) Math.max(1, Math.round(spdW / totW * bst)) + 10);
            setSpeed((int) Math.max(1, Math.round(speW / totW * bst)) + 10);

            // Fix up special too
            setSpecial((int) Math.ceil((getSpatk() + getSpdef()) / 2.0f));
        }

        // Check for something we can't store
        if (getHp() > 255 || getAttack() > 255 || getDefense() > 255 || getSpatk() > 255 || getSpdef() > 255 || getSpeed() > 255) {
            // re roll
            randomizeStatsWithinBST(random);
        }

    }

    public void copyRandomizedStatsUpEvolution(Pokemon evolvesFrom) {
        double ourBST = bst();
        double theirBST = evolvesFrom.bst();

        double bstRatio = ourBST / theirBST;

        setHp((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getHp() * bstRatio))));
        setAttack((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getAttack() * bstRatio))));
        setDefense((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getDefense() * bstRatio))));
        setSpeed((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getSpeed() * bstRatio))));
        setSpatk((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getSpatk() * bstRatio))));
        setSpdef((int) Math.min(255, Math.max(1, Math.round(evolvesFrom.getSpdef() * bstRatio))));

        setSpecial((int) Math.ceil((getSpatk() + getSpdef()) / 2.0f));
    }

    public int bst() {
        return getHp() + getAttack() + getDefense() + getSpatk() + getSpdef() + getSpeed();
    }

    public int bstForPowerLevels() {
        // Take into account Shedinja's purposefully nerfed HP
        if (getNumber() == 292) {
            return (getAttack() + getDefense() + getSpatk() + getSpdef() + getSpeed()) * 6 / 5;
        } else {
            return getHp() + getAttack() + getDefense() + getSpatk() + getSpdef() + getSpeed();
        }
    }

    @Override
    public String toString() {
        return "Pokemon [name=" + getName() + ", number=" + getNumber() + ", primaryType=" + getPrimaryType() + ", secondaryType="
                + getSecondaryType() + ", hp=" + getHp() + ", attack=" + getAttack() + ", defense=" + getDefense() + ", spatk=" + getSpatk()
                + ", spdef=" + getSpdef() + ", speed=" + getSpeed() + "]";
    }

    public String toStringRBY() {
        return "Pokemon [name=" + getName() + ", number=" + getNumber() + ", primaryType=" + getPrimaryType() + ", secondaryType="
                + getSecondaryType() + ", hp=" + getHp() + ", attack=" + getAttack() + ", defense=" + getDefense() + ", special=" + getSpecial()
                + ", speed=" + getSpeed() + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Pokemon)) {
            return false;
        }
        Pokemon other = (Pokemon) obj;
        return number == other.number;
    }

    @Override
    public int compareTo(Pokemon o) {
        return Integer.compare(getNumber(), o.getNumber());
    }

    public boolean isLegendary() {
        return legendaries.contains(this.getNumber());
    }

}
