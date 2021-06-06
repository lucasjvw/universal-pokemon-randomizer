package com.dabomstew.pkrandom.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dabomstew.pkrandom.Modifier;
import com.dabomstew.pkrandom.Settings;
import com.dabomstew.pkrandom.pokemon.IngameTrade;
import com.dabomstew.pkrandom.pokemon.ItemList;
import com.dabomstew.pkrandom.pokemon.Pokemon;
import com.dabomstew.pkrandom.romhandlers.RomHandler;

public class TradeModifier implements Modifier {

    private final RomHandler romHandler;
    private final Settings settings;
    private final Random random;

    public TradeModifier(RomHandler romHandler, Settings settings, Random random) {
        this.romHandler = romHandler;
        this.settings = settings;
        this.random = random;
    }

    @Override
    public void modify() {
        boolean randomizedRequested;
        if (settings.getInGameTradesMod() == Settings.InGameTradesMod.RANDOMIZE_GIVEN) {
            randomizedRequested = false;
        } else if (settings.getInGameTradesMod() == Settings.InGameTradesMod.RANDOMIZE_GIVEN_AND_REQUESTED) {
            randomizedRequested = true;
        } else {
            return; // no mod
        }
        
        // Process trainer names
        List<String> trainerNames = new ArrayList<>();
        // Check for the file
        if (settings.isRandomizeInGameTradesOTs()) {
            int maxOT = romHandler.maxTradeOTNameLength();
            for (String trainername : settings.getCustomNames().getTrainerNames()) {
                int len = romHandler.internalStringLength(trainername);
                if (len <= maxOT && !trainerNames.contains(trainername)) {
                    trainerNames.add(trainername);
                }
            }
        }

        // Process nicknames
        List<String> nicknames = new ArrayList<String>();
        // Check for the file
        if (settings.isRandomizeInGameTradesNicknames()) {
            int maxNN = romHandler.maxTradeNicknameLength();
            for (String nickname : settings.getCustomNames().getPokemonNicknames()) {
                int len = romHandler.internalStringLength(nickname);
                if (len <= maxNN && !nicknames.contains(nickname)) {
                    nicknames.add(nickname);
                }
            }
        }

        // get old trades
        List<IngameTrade> trades = romHandler.getIngameTrades();
        List<Pokemon> usedRequests = new ArrayList<>();
        List<Pokemon> usedGivens = new ArrayList<>();
        List<String> usedOTs = new ArrayList<>();
        List<String> usedNicknames = new ArrayList<>();
        ItemList possibleItems = romHandler.getAllowedItems();

        int nickCount = nicknames.size();
        int trnameCount = trainerNames.size();

        for (IngameTrade trade : trades) {
            // pick new given pokemon
            Pokemon oldgiven = trade.givenPokemon;
            Pokemon given = romHandler.randomPokemon();
            while (usedGivens.contains(given)) {
                given = romHandler.randomPokemon();
            }
            usedGivens.add(given);
            trade.givenPokemon = given;

            // requested pokemon?
            if (oldgiven == trade.requestedPokemon) {
                // preserve trades for the same pokemon
                trade.requestedPokemon = given;
            } else if (randomizedRequested) {
                Pokemon request = romHandler.randomPokemon();
                while (usedRequests.contains(request) || request == given) {
                    request = romHandler.randomPokemon();
                }
                usedRequests.add(request);
                trade.requestedPokemon = request;
            }

            // nickname?
            if (settings.isRandomizeInGameTradesNicknames() && nickCount > usedNicknames.size()) {
                String nickname = nicknames.get(this.random.nextInt(nickCount));
                while (usedNicknames.contains(nickname)) {
                    nickname = nicknames.get(this.random.nextInt(nickCount));
                }
                usedNicknames.add(nickname);
                trade.nickname = nickname;
            } else if (trade.nickname.equalsIgnoreCase(oldgiven.getName())) {
                // change the name for sanity
                trade.nickname = trade.givenPokemon.getName();
            }

            if (settings.isRandomizeInGameTradesOTs() && trnameCount > usedOTs.size()) {
                String ot = trainerNames.get(this.random.nextInt(trnameCount));
                while (usedOTs.contains(ot)) {
                    ot = trainerNames.get(this.random.nextInt(trnameCount));
                }
                usedOTs.add(ot);
                trade.otName = ot;
                trade.otId = this.random.nextInt(65536);
            }

            if (settings.isRandomizeInGameTradesIVs()) {
                int maxIV = romHandler.hasDVs() ? 16 : 32;
                for (int i = 0; i < trade.ivs.length; i++) {
                    trade.ivs[i] = this.random.nextInt(maxIV);
                }
            }

            if (settings.isRandomizeInGameTradesItems()) {
                trade.item = possibleItems.randomItem(this.random);
            }
        }

        // things that the game doesn't support should just be ignored
        romHandler.setIngameTrades(trades);
    }

}
