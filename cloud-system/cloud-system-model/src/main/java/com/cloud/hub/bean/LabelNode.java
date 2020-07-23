package com.cloud.hub.bean;

public class LabelNode {
    public LabelNode() {

    }

    public LabelNode(String label, Long value) {
        this.label = label;
        this.value = value;
    }

    private String label;

    private Long  value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
