package gay.aliahx.mixtape.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import gay.aliahx.mixtape.Mixtape;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.lang.reflect.Field;

import static gay.aliahx.mixtape.config.ModConfig.getCategory;
import static gay.aliahx.mixtape.config.ModConfig.getCategoryDefaults;

public class YACLImplementation {
    public static Screen generateConfigScreen(Screen parent) {
        YetAnotherConfigLib.Builder builder = YetAnotherConfigLib.createBuilder().title(getText("config", "title"));

        Field[] categories = ModConfig.class.getFields();

        for(Field category : categories) {
            if(!category.getName().equals("GSON") && !category.getName().equals("CONFIG_FILE") && !category.getName().equals("INSTANCE")) {
                ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder().name(getText("category", category.getName()));
                Field[] fields = category.getType().getFields();
                for(Field field : fields) {
                    Object categoryDefaults = getCategoryDefaults(category.getName());
                    Object defaultValue = false;
                    try {defaultValue = categoryDefaults.getClass().getField(field.getName()).get(categoryDefaults);} catch (Exception ignored) {}
                    Object currentCategory = getCategory(category.getName());
                    switch (field.getType().toString()) {
                        case "boolean" ->
                            categoryBuilder = categoryBuilder.option(getBooleanOption(category.getName(), field.getName())
                                .binding((Boolean) defaultValue, () -> {
                                    try {
                                        return (Boolean) currentCategory.getClass().getField(field.getName()).get(currentCategory);
                                    } catch (Exception ignored) {
                                        return false;
                                    }
                                }, value -> {
                                    try {
                                        currentCategory.getClass().getField(field.getName()).set(currentCategory, value);
                                    } catch (Exception ignored) {}
                                })
                                .build());
                        case "int" ->
                            categoryBuilder = categoryBuilder.option(getOption(Integer.class, category.getName(), field.getName())
                                .controller(YACLImplementation::getIntegerSlider)
                                .binding((Integer) defaultValue, () -> {
                                    try {
                                        return (Integer) currentCategory.getClass().getField(field.getName()).get(currentCategory);
                                    } catch (Exception ignored) {
                                        return 0;
                                    }
                                }, value -> {
                                    try {
                                        currentCategory.getClass().getField(field.getName()).set(currentCategory, value);
                                    } catch (Exception ignored) {}
                                })
                                .build());
                        case "float" ->
                            categoryBuilder = categoryBuilder.option(getOption(Float.class, category.getName(), field.getName())
                                .controller(YACLImplementation::getFloatSlider)
                                .binding((Float) defaultValue, () -> {
                                    try {
                                        return (Float) currentCategory.getClass().getField(field.getName()).get(currentCategory);
                                    } catch (Exception ignored) {
                                        return 0F;
                                    }
                                }, value -> {
                                    try {
                                        currentCategory.getClass().getField(field.getName()).set(currentCategory, value);
                                    } catch (Exception ignored) {}
                                })
                                .build());
                        case "long" ->
                            categoryBuilder = categoryBuilder.option(getOption(Long.class, category.getName(), field.getName())
                                .controller(YACLImplementation::getLongSlider)
                                .binding((Long) defaultValue, () -> {
                                    try {
                                        return (Long) currentCategory.getClass().getField(field.getName()).get(currentCategory);
                                    } catch (Exception ignored) {
                                        return 0L;
                                    }
                                }, value -> {
                                    try {
                                        currentCategory.getClass().getField(field.getName()).set(currentCategory, value);
                                    } catch (Exception ignored) {}
                                })
                                .build());
                        case "class gay.aliahx.mixtape.config.ModConfig$MusicType" ->
                            categoryBuilder = categoryBuilder.option(getOption(ModConfig.MusicType.class, category.getName(), field.getName())
                                .controller(YACLImplementation::getEnumSelector)
                                .binding((ModConfig.MusicType) defaultValue, () -> {
                                    try {
                                        return (ModConfig.MusicType) currentCategory.getClass().getField(field.getName()).get(currentCategory);
                                    } catch (Exception ignored) {
                                        return ModConfig.MusicType.AUTOMATIC;
                                    }
                                }, value -> {
                                    try {
                                        currentCategory.getClass().getField(field.getName()).set(currentCategory, value);
                                    } catch (Exception ignored) {}
                                })
                                .build());
                        default -> Mixtape.LOGGER.warn("Unknown config option type: " + field.getType() + ". for " + field.getName());
                    }
                }
                builder = builder.category(categoryBuilder.build());
            }
        }

        return builder.save(ModConfig.INSTANCE::save).build().generateScreen(parent);
    }

    private static <T> Option.Builder<T> getOption(Class<T> ignored, String category, String key) {
        Option.Builder<T> builder = Option.<T>createBuilder().name(getText(category, key));
        var descBuilder = OptionDescription.createBuilder();
        descBuilder.text(getText(category, key + ".description"));
        builder.description(descBuilder.build());
        return builder;
    }

    private static Option.Builder<Boolean> getBooleanOption(String category, String key) {
        return getOption(Boolean.class, category, key).controller(TickBoxControllerBuilder::create);
    }

    private static IntegerSliderControllerBuilder getIntegerSlider(Option<Integer> option) {
        return IntegerSliderControllerBuilder.create(option).range(0, 24000).step(20);
    }

    private static FloatSliderControllerBuilder getFloatSlider(Option<Float> option) {
        return FloatSliderControllerBuilder.create(option).range((float) 0, (float) 400).step((float) 1);
    }

    private static LongSliderControllerBuilder getLongSlider(Option<Long> option) {
        return LongSliderControllerBuilder.create(option).range((long) -12, (long) 12).step((long) 1);
    }

    private static EnumControllerBuilder getEnumSelector(Option<ModConfig.MusicType> option) {
        return EnumControllerBuilder.create(option).enumClass(ModConfig.MusicType.class);
    }

    private static MutableText getText(String category, String key) {
        return Text.translatable("config.mixtape." + category + "." + key);
    }
}
