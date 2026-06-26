package com.raxxo310.basicmod.block.screen;

import com.raxxo310.basicmod.BasicMod;
import com.raxxo310.basicmod.block.entity.ExampleBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ExampleBlockConfigScreen extends Screen {
    private static final int WIDTH = 256;
    private static final int HEIGHT = 200;
    
    private final BlockPos blockPos;
    private EditBox teamInput;
    private Button confirmButton;
    private Button cancelButton;
    private ExampleBlockEntity blockEntity;
    private String currentTeam;

    public ExampleBlockConfigScreen(ExampleBlockEntity blockEntity, BlockPos blockPos) {
        super(Component.literal("Configure Example Block"));
        this.blockEntity = blockEntity;
        this.blockPos = blockPos;
        this.currentTeam = blockEntity.getTeam();
    }

    @Override
    protected void init() {
        int centerX = (this.width - WIDTH) / 2;
        int centerY = (this.height - HEIGHT) / 2;
        
        // Team input field
        this.teamInput = new EditBox(this.font, centerX + 10, centerY + 50, WIDTH - 20, 20, Component.literal("Team"));
        this.teamInput.setValue(this.currentTeam);
        this.teamInput.setMaxLength(32);
        this.addRenderableWidget(this.teamInput);
        
        // Confirm button
        this.confirmButton = this.addRenderableWidget(new Button.Builder(
                Component.literal("Confirm"),
                button -> this.onConfirm()
        ).bounds(centerX + 20, centerY + 100, 90, 20).build());
        
        // Cancel button
        this.cancelButton = this.addRenderableWidget(new Button.Builder(
                Component.literal("Cancel"),
                button -> this.onClose()
        ).bounds(centerX + 120, centerY + 100, 90, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int centerX = (this.width - WIDTH) / 2;
        int centerY = (this.height - HEIGHT) / 2;
        
        // Background
        guiGraphics.fill(centerX, centerY, centerX + WIDTH, centerY + HEIGHT, 0xFF8B8B8B);
        guiGraphics.fill(centerX + 1, centerY + 1, centerX + WIDTH - 1, centerY + HEIGHT - 1, 0xFF000000);
        
        // Title
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, centerY + 10, 0xFFFFFF);
        
        // Labels
        guiGraphics.drawString(this.font, "Team Name:", centerX + 10, centerY + 35, 0xFFFFFF);
        guiGraphics.drawString(this.font, "Block Position: " + this.blockPos.toShortString(), centerX + 10, centerY + 130, 0xAAAAA);
        
        this.teamInput.render(guiGraphics, mouseX, mouseY, partialTick);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void onConfirm() {
        String newTeam = this.teamInput.getValue().trim();
        
        if (this.minecraft != null && this.minecraft.player != null) {
            // Send update to server via packet
            BasicMod.TEAM_CONFIG_PACKET.sendToServer(new ExampleBlockTeamConfigPayload(this.blockPos, newTeam));
            this.onClose();
        }
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
