package dev.lazycat.lazyChat.api.chatSystem.configs;

import java.util.List;

public class BlacklistConfig {
    private boolean global = true;
    private boolean colors = false;
    private boolean gradients = false;
    private List<String> tags = List.of();

    public boolean isGlobal() { return global; }
    public void setGlobal(boolean global) { this.global = global; }
    public boolean isColors() { return colors; }
    public void setColors(boolean colors) { this.colors = colors; }
    public boolean isGradients() { return gradients; }
    public void setGradients(boolean gradients) { this.gradients = gradients; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}