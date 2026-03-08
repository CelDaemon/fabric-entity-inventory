package entityinventory.client;

import entityinventory.client.datagen.EnglishLanguageProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class EntityInventoryDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator generator) {
		final var pack = generator.createPack();
		pack.addProvider(EnglishLanguageProvider::new);
	}
}
