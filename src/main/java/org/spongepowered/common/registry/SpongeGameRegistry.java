/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.registry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.api.GameProfile;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.status.Favicon;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.chat.ChatType;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.translation.Translation;
import org.spongepowered.api.text.translation.locale.Locales;
import org.spongepowered.common.status.SpongeFavicon;
import org.spongepowered.common.text.SpongeTextFactory;
import org.spongepowered.common.text.chat.SpongeChatType;
import org.spongepowered.common.text.format.SpongeTextColor;
import org.spongepowered.common.text.format.SpongeTextStyle;
import org.spongepowered.common.text.translation.SpongeTranslation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.inject.Singleton;

@Singleton
public abstract class SpongeGameRegistry implements GameRegistry {

    public static final Map<String, TextColor> textColorMappings = Maps.newHashMap();
    public static final Map<EnumChatFormatting, SpongeTextColor> enumChatColor = Maps.newEnumMap(EnumChatFormatting.class);

    public static final ImmutableMap<String, TextStyle> textStyleMappings = new ImmutableMap.Builder<String, TextStyle>()
            .put("BOLD", SpongeTextStyle.of(EnumChatFormatting.BOLD))
            .put("ITALIC", SpongeTextStyle.of(EnumChatFormatting.ITALIC))
            .put("UNDERLINE", SpongeTextStyle.of(EnumChatFormatting.UNDERLINE))
            .put("STRIKETHROUGH", SpongeTextStyle.of(EnumChatFormatting.STRIKETHROUGH))
            .put("OBFUSCATED", SpongeTextStyle.of(EnumChatFormatting.OBFUSCATED))
            .put("RESET", SpongeTextStyle.of(EnumChatFormatting.RESET))
            .build();
    private static final ImmutableMap<String, ChatType> chatTypeMappings = new ImmutableMap.Builder<String, ChatType>()
            .put("CHAT", new SpongeChatType((byte) 0))
            .put("SYSTEM", new SpongeChatType((byte) 1))
            .put("ACTION_BAR", new SpongeChatType((byte) 2))
            .build();
    private static final ImmutableMap<String, Locale> localeCodeMappings = ImmutableMap.<String, Locale>builder()
            .put("af_ZA", new Locale("af", "ZA"))
            .put("ar_SA", new Locale("ar", "SA"))
            .put("ast_ES", new Locale("ast", "ES"))
            .put("az_AZ", new Locale("az", "AZ"))
            .put("bg_BG", new Locale("bg", "BG"))
            .put("ca_ES", new Locale("ca", "ES"))
            .put("cs_CZ", new Locale("cs", "CZ"))
            .put("cy_GB", new Locale("cy", "GB"))
            .put("da_DK", new Locale("da", "DK"))
            .put("de_DE", new Locale("de", "DE"))
            .put("el_GR", new Locale("el", "GR"))
            .put("en_AU", new Locale("en", "AU"))
            .put("en_CA", new Locale("en", "CA"))
            .put("en_GB", new Locale("en", "GB"))
            .put("en_PT", new Locale("en", "PT"))
            .put("en_US", new Locale("en", "US"))
            .put("eo_UY", new Locale("eo", "UY"))
            .put("es_AR", new Locale("es", "AR"))
            .put("es_ES", new Locale("es", "ES"))
            .put("es_MX", new Locale("es", "MX"))
            .put("es_UY", new Locale("es", "UY"))
            .put("es_VE", new Locale("es", "VE"))
            .put("et_EE", new Locale("et", "EE"))
            .put("eu_ES", new Locale("eu", "ES"))
            .put("fa_IR", new Locale("fa", "IR"))
            .put("fi_FI", new Locale("fi", "FI"))
            .put("fil_PH", new Locale("fil", "PH"))
            .put("fr_CA", new Locale("fr", "CA"))
            .put("fr_FR", new Locale("fr", "FR"))
            .put("ga_IE", new Locale("ga", "IE"))
            .put("gl_ES", new Locale("gl", "ES"))
            .put("gv_IM", new Locale("gv", "IM"))
            .put("he_IL", new Locale("he", "IL"))
            .put("hi_IN", new Locale("hi", "IN"))
            .put("hr_HR", new Locale("hr", "HR"))
            .put("hu_HU", new Locale("hu", "HU"))
            .put("hy_AM", new Locale("hy", "AM"))
            .put("id_ID", new Locale("id", "ID"))
            .put("is_IS", new Locale("is", "IS"))
            .put("it_IT", new Locale("it", "IT"))
            .put("ja_JP", new Locale("ja", "JP"))
            .put("ka_GE", new Locale("ka", "GE"))
            .put("ko_KR", new Locale("ko", "KR"))
            .put("kw_GB", new Locale("kw", "GB"))
            .put("la_LA", new Locale("la", "LA"))
            .put("lb_LU", new Locale("lb", "LU"))
            .put("lt_LT", new Locale("lt", "LT"))
            .put("lv_LV", new Locale("lv", "LV"))
            .put("mi_NZ", new Locale("mi", "NZ"))
            .put("ms_MY", new Locale("ms", "MY"))
            .put("mt_MT", new Locale("mt", "MT"))
            .put("nds_DE", new Locale("nds", "DE"))
            .put("nl_NL", new Locale("nl", "NL"))
            .put("nn_NO", new Locale("nn", "NO"))
            .put("no_NO", new Locale("no", "NO"))
            .put("oc_FR", new Locale("oc", "FR"))
            .put("pl_PL", new Locale("pl", "PL"))
            .put("pt_BR", new Locale("pt", "BR"))
            .put("pt_PT", new Locale("pt", "PT"))
            .put("qya_AA", new Locale("qya", "AA"))
            .put("ro_RO", new Locale("ro", "RO"))
            .put("ru_RU", new Locale("ru", "RU"))
            .put("se_NO", new Locale("se", "NO"))
            .put("sk_SK", new Locale("sk", "SK"))
            .put("sl_SI", new Locale("sl", "SI"))
            .put("sr_SP", new Locale("sr", "SP"))
            .put("sv_SE", new Locale("sv", "SE"))
            .put("th_TH", new Locale("th", "TH"))
            .put("tlh_AA", new Locale("tlh", "AA"))
            .put("tr_TR", new Locale("tr", "TR"))
            .put("uk_UA", new Locale("uk", "UA"))
            .put("val_ES", new Locale("val", "ES"))
            .put("vi_VN", new Locale("vi", "VN"))
            .put("zh_CN", new Locale("zh", "CN"))
            .put("zh_TW", new Locale("zh", "TW"))
            .build();
    private static final ImmutableMap<String, Locale> localeMappings = ImmutableMap.<String, Locale>builder()
            .put("AFRIKAANS", localeCodeMappings.get("af_ZA"))
            .put("ARABIC", localeCodeMappings.get("ar_SA"))
            .put("ASTURIAN", localeCodeMappings.get("ast_ES"))
            .put("AZERBAIJANI", localeCodeMappings.get("az_AZ"))
            .put("BULGARIAN", localeCodeMappings.get("bg_BG"))
            .put("CATALAN", localeCodeMappings.get("ca_ES"))
            .put("CZECH", localeCodeMappings.get("cs_CZ"))
            .put("WELSH", localeCodeMappings.get("cy_GB"))
            .put("DANISH", localeCodeMappings.get("da_DK"))
            .put("GERMAN", localeCodeMappings.get("de_DE"))
            .put("GREEK", localeCodeMappings.get("el_GR"))
            .put("AUSTRALIAN_ENGLISH", localeCodeMappings.get("en_AU"))
            .put("CANADIAN_ENGLISH", localeCodeMappings.get("en_CA"))
            .put("BRITISH_ENGLISH", localeCodeMappings.get("en_GB"))
            .put("PIRATE_ENGLISH", localeCodeMappings.get("en_PT"))
            .put("ENGLISH", localeCodeMappings.get("en_US"))
            .put("ESPERANTO", localeCodeMappings.get("eo_UY"))
            .put("ARGENTINIAN_SPANISH", localeCodeMappings.get("es_AR"))
            .put("SPANISH", localeCodeMappings.get("es_ES"))
            .put("MEXICAN_SPANISH", localeCodeMappings.get("es_MX"))
            .put("URUGUAYAN_SPANISH", localeCodeMappings.get("es_UY"))
            .put("VENEZUELAN_SPANISH", localeCodeMappings.get("es_VE"))
            .put("ESTONIAN", localeCodeMappings.get("et_EE"))
            .put("BASQUE", localeCodeMappings.get("eu_ES"))
            .put("PERSIAN", localeCodeMappings.get("fa_IR"))
            .put("FINNISH", localeCodeMappings.get("fi_FI"))
            .put("FILIPINO", localeCodeMappings.get("fil_PH"))
            .put("CANADIAN_FRENCH", localeCodeMappings.get("fr_CA"))
            .put("FRENCH", localeCodeMappings.get("fr_FR"))
            .put("IRISH", localeCodeMappings.get("ga_IE"))
            .put("GALICIAN", localeCodeMappings.get("gl_ES"))
            .put("MANX", localeCodeMappings.get("gv_IM"))
            .put("HEBREW", localeCodeMappings.get("he_IL"))
            .put("HINDI", localeCodeMappings.get("hi_IN"))
            .put("CROATIAN", localeCodeMappings.get("hr_HR"))
            .put("HUNGARIAN", localeCodeMappings.get("hu_HU"))
            .put("ARMENIAN", localeCodeMappings.get("hy_AM"))
            .put("INDONESIAN", localeCodeMappings.get("id_ID"))
            .put("ICELANDIC", localeCodeMappings.get("is_IS"))
            .put("ITALIAN", localeCodeMappings.get("it_IT"))
            .put("JAPANESE", localeCodeMappings.get("ja_JP"))
            .put("GEORGIAN", localeCodeMappings.get("ka_GE"))
            .put("KOREAN", localeCodeMappings.get("ko_KR"))
            .put("CORNISH", localeCodeMappings.get("kw_GB"))
            .put("LATIN", localeCodeMappings.get("la_LA"))
            .put("LUXEMBOURGISH", localeCodeMappings.get("lb_LU"))
            .put("LITHUANIAN", localeCodeMappings.get("lt_LT"))
            .put("LATVIAN", localeCodeMappings.get("lv_LV"))
            .put("MAORI", localeCodeMappings.get("mi_NZ"))
            .put("MALAY", localeCodeMappings.get("ms_MY"))
            .put("MALTESE", localeCodeMappings.get("mt_MT"))
            .put("LOW_GERMAN", localeCodeMappings.get("nds_DE"))
            .put("DUTCH", localeCodeMappings.get("nl_NL"))
            .put("NORWEGIAN_NYNORSK", localeCodeMappings.get("nn_NO"))
            .put("NORWEGIAN", localeCodeMappings.get("no_NO"))
            .put("OCCITAN", localeCodeMappings.get("oc_FR"))
            .put("POLISH", localeCodeMappings.get("pl_PL"))
            .put("BRAZILIAN_PORTUGUESE", localeCodeMappings.get("pt_BR"))
            .put("PORTUGUESE", localeCodeMappings.get("pt_PT"))
            .put("QUENYA", localeCodeMappings.get("qya_AA"))
            .put("ROMANIAN", localeCodeMappings.get("ro_RO"))
            .put("RUSSIAN", localeCodeMappings.get("ru_RU"))
            .put("NORTHERN_SAMI", localeCodeMappings.get("se_NO"))
            .put("SLOVAK", localeCodeMappings.get("sk_SK"))
            .put("SLOVENE", localeCodeMappings.get("sl_SI"))
            .put("SERBIAN", localeCodeMappings.get("sr_SP"))
            .put("SWEDISH", localeCodeMappings.get("sv_SE"))
            .put("THAI", localeCodeMappings.get("th_TH"))
            .put("KLINGON", localeCodeMappings.get("tlh_AA"))
            .put("TURKISH", localeCodeMappings.get("tr_TR"))
            .put("UKRAINIAN", localeCodeMappings.get("uk_UA"))
            .put("VALENCIAN", localeCodeMappings.get("val_ES"))
            .put("VIETNAMESE", localeCodeMappings.get("vi_VN"))
            .put("SIMPLIFIED_CHINESE", localeCodeMappings.get("zh_CN"))
            .put("TRADITIONAL_CHINESE", localeCodeMappings.get("zh_TW"))
            .build();

    @Override
    public Optional<TextColor> getTextColor(String name) {
        return Optional.fromNullable(textColorMappings.get(name));
    }

    @Override
    public Collection<TextColor> getTextColors() {
        return Collections.unmodifiableCollection(textColorMappings.values());
    }

    @Override
    public Optional<TextStyle> getTextStyle(String name) {
        return Optional.fromNullable(textStyleMappings.get(name));
    }

    @Override
    public Collection<TextStyle> getTextStyles() {
        return Collections.unmodifiableCollection(textStyleMappings.values());
    }

    @Override
    public Optional<ChatType> getChatType(String name) {
        return Optional.fromNullable(chatTypeMappings.get(name));
    }

    @Override
    public Collection<ChatType> getChatTypes() {
        return Collections.unmodifiableCollection(chatTypeMappings.values());
    }

    @Override
    public Optional<Locale> getLocale(String name) {
        return Optional.fromNullable(localeMappings.get(name));
    }

    @Override
    public Optional<Locale> getLocaleById(String id) {
        return Optional.fromNullable(localeCodeMappings.get(id));
    }

    @Override
    public Collection<Locale> getLocales() {
        return Collections.unmodifiableCollection(localeMappings.values());
    }

    @Override
    public Optional<Translation> getTranslationById(String id) {
        return Optional.<Translation>of(new SpongeTranslation(id));
    }

    @Override
    public Favicon loadFavicon(String raw) throws IOException {
        return SpongeFavicon.load(raw);
    }

    @Override
    public Favicon loadFavicon(File file) throws IOException {
        return SpongeFavicon.load(file);
    }

    @Override
    public Favicon loadFavicon(URL url) throws IOException {
        return SpongeFavicon.load(url);
    }

    @Override
    public Favicon loadFavicon(InputStream in) throws IOException {
        return SpongeFavicon.load(in);
    }

    @Override
    public Favicon loadFavicon(BufferedImage image) throws IOException {
        return SpongeFavicon.load(image);
    }

    @Override
    public GameProfile createGameProfile(UUID uuid, String name) {
        return (GameProfile) new com.mojang.authlib.GameProfile(uuid, name);
    }

    private static void addTextColor(EnumChatFormatting handle, Color color) {
        SpongeTextColor spongeColor = new SpongeTextColor(handle, color);
        textColorMappings.put(handle.name(), spongeColor);
        enumChatColor.put(handle, spongeColor);
    }

    protected void setTextColors() {
        addTextColor(EnumChatFormatting.BLACK, Color.BLACK);
        addTextColor(EnumChatFormatting.DARK_BLUE, new Color(0x0000AA));
        addTextColor(EnumChatFormatting.DARK_GREEN, new Color(0x00AA00));
        addTextColor(EnumChatFormatting.DARK_AQUA, new Color(0x00AAAA));
        addTextColor(EnumChatFormatting.DARK_RED, new Color(0xAA0000));
        addTextColor(EnumChatFormatting.DARK_PURPLE, new Color(0xAA00AA));
        addTextColor(EnumChatFormatting.GOLD, new Color(0xFFAA00));
        addTextColor(EnumChatFormatting.GRAY, new Color(0xAAAAAA));
        addTextColor(EnumChatFormatting.DARK_GRAY, new Color(0x555555));
        addTextColor(EnumChatFormatting.BLUE, new Color(0x5555FF));
        addTextColor(EnumChatFormatting.GREEN, new Color(0x55FF55));
        addTextColor(EnumChatFormatting.AQUA, new Color(0x00FFFF));
        addTextColor(EnumChatFormatting.RED, new Color(0xFF5555));
        addTextColor(EnumChatFormatting.LIGHT_PURPLE, new Color(0xFF55FF));
        addTextColor(EnumChatFormatting.YELLOW, new Color(0xFFFF55));
        addTextColor(EnumChatFormatting.WHITE, Color.WHITE);
        addTextColor(EnumChatFormatting.RESET, Color.WHITE);

        RegistryHelper.mapFields(TextColors.class, textColorMappings);
        RegistryHelper.mapFields(ChatTypes.class, chatTypeMappings);
        RegistryHelper.mapFields(TextStyles.class, textStyleMappings);
    }

    protected void setTextFactory() {
        RegistryHelper.setFactory(Texts.class, new SpongeTextFactory());
    }

    protected void setLocales() {
        RegistryHelper.mapFields(Locales.class, localeMappings);
    }

}
