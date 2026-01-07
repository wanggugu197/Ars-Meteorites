package com.arsmeteorites.arsmeteorites.common.recipe.builder;

import net.minecraft.util.GsonHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public record dataMeteoriteRecipe(
                                  String inputItemId,
                                  double source,
                                  int model,
                                  String catalysts,
                                  String[] meteoriteBlockIds,
                                  int[] weights,
                                  int[] layer) {

    public static dataMeteoriteRecipe fromJson(JsonObject json) {
        String inputItemId = GsonHelper.getAsString(json, "input");
        double source = GsonHelper.getAsDouble(json, "source");

        int model = GsonHelper.getAsInt(json, "model", 0);

        String catalysts = GsonHelper.getAsString(json, "catalysts");

        JsonArray meteoritesArray = GsonHelper.getAsJsonArray(json, "meteorites");
        JsonArray weightsArray = GsonHelper.getAsJsonArray(json, "weights");

        String[] blockIds = new String[meteoritesArray.size()];
        int[] weights = new int[weightsArray.size()];

        for (int i = 0; i < meteoritesArray.size(); i++) {
            blockIds[i] = meteoritesArray.get(i).getAsString();
            weights[i] = weightsArray.get(i).getAsInt();
        }

        JsonArray layerArray = GsonHelper.getAsJsonArray(json, "layer", null);

        int[] layer;
        if (layerArray != null) {
            layer = new int[layerArray.size()];
            for (int i = 0; i < layerArray.size(); i++) layer[i] = layerArray.get(i).getAsInt();
        } else layer = new int[0];

        return new dataMeteoriteRecipe(inputItemId, source, model, catalysts, blockIds, weights, layer);
    }
}
