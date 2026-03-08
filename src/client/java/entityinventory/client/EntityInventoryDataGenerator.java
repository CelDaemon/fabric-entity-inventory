package entityinventory.client;

import entityinventory.client.datagen.EnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EntityInventoryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		final var pack = fabricDataGenerator.createPack();
		pack.addProvider(EnglishLanguageProvider::new);
	}
}
