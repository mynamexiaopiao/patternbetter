package com.xiaopiao.patternbetter;


import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
public class MyConfigScreen extends Screen {
    private final Screen parent;

    private EditBox numberBox;

    private boolean isPageButton;
    private boolean isJumpBox;
    private boolean isPatternsInto;

    public MyConfigScreen(Screen parent) {
        super(Component.literal("My Mod Config"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // 读取当前配置值
        isPageButton = ModConfig.pageButton;
        isJumpBox = ModConfig.jumpBox;
        isPatternsInto = ModConfig.patternsInto;
        String pageButtonText = "patternbetter.configuration.pagebutton";
        String jumpBoxText = "patternbetter.configuration.jumpbox";
        String patternsIntoText = "patternbetter.configuration.patternsinto";
        String slotValueText = "patternbetter.configuration.slotvalue";

        int wid = this.width / 2 - 150;
        int baseHeight = this.height / 4;

        // 开关按钮 - 统一使用相同的x坐标和间距
        this.addRenderableWidget(Button.builder(
                Component.translatable(jumpBoxText).append("：").append(String.valueOf(isJumpBox)),
                btn -> {
                    isJumpBox = !isJumpBox;
                    btn.setMessage(Component.translatable(jumpBoxText).append("：").append(String.valueOf(isJumpBox)));
                }
        ).bounds(wid, baseHeight, 300, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable(patternsIntoText).append("：").append(String.valueOf(isPatternsInto)),
                btn -> {
                    isPatternsInto = !isPatternsInto;
                    btn.setMessage(Component.translatable(patternsIntoText).append("：").append(String.valueOf(isPatternsInto)));
                }
        ).bounds(wid, baseHeight + 25, 300, 20).build());

        this.addRenderableWidget(Button.builder(
                Component.translatable(pageButtonText).append("：").append(String.valueOf(isPageButton)),
                btn -> {
                    isPageButton = !isPageButton;
                    btn.setMessage(Component.translatable(pageButtonText).append("：").append(String.valueOf(isPageButton)));
                }
        ).bounds(wid, baseHeight + 50, 300, 20).build());

        // 数字输入框
        numberBox = new EditBox(this.font,
                wid, baseHeight + 75, 300, 20,
                Component.translatable(slotValueText));
        numberBox.setValue(String.valueOf(ModConfig.SLOT_VALUE.get()));
        numberBox.setHint(Component.literal("输入数字")); // 添加提示
        this.addRenderableWidget(numberBox);

        // 保存按钮
        this.addRenderableWidget(Button.builder(Component.literal("保存并返回"), btn -> {
            saveConfig();
            this.minecraft.setScreen(parent);
        }).bounds(wid, this.height - 30, 300, 20).build());
    }



    private void saveConfig() {
        try {
            int num = Integer.parseInt(numberBox.getValue());
            ModConfig.SLOT_VALUE.set(num);

        } catch (NumberFormatException e) {
            // 输入错误不保存
        }
        ModConfig.PAGE_BUTTON.set(isPageButton);
        ModConfig.JUMP_BOX.set(isJumpBox);
        ModConfig.PATTERNS_INTO.set(isPatternsInto);

        // 保存到磁盘
        ModConfig.CONFIG.save();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, "patternbetter config", this.width / 2, 20, 0xFFFFFF);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        numberBox.render(guiGraphics, mouseX, mouseY, partialTicks);
    }
}