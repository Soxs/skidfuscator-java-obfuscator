package dev.skidfuscator.obfuscator.phantom.jghost.tree;

import com.google.gson.annotations.SerializedName;
import dev.skidfuscator.obfuscator.phantom.jghost.GhostReader;
import dev.skidfuscator.obfuscator.skidasm.builder.MethodNodeBuilder;
import org.mapleir.asm.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GhostMethodNode implements GhostReader<MethodNode> {
    @SerializedName("name")
    private String name;

    @SerializedName("access")
    private int access;

    @SerializedName("desc")
    private String desc;

    @SerializedName("signature")
    private String signature;

    @SerializedName("exceptions")
    private List<String> exceptions;

    @SerializedName("visibleAnnotations")
    private List<GhostAnnotationNode> visibleAnnotations;

    @SerializedName("invisibleAnnotations")
    private List<GhostAnnotationNode> invisibleAnnotations;

    public GhostMethodNode() {
        super();
    }

    private GhostMethodNode(final MethodNode node) {
        this.access = node.access;
        this.name = node.name;
        this.desc = node.desc;
        this.signature = node.signature;
        this.exceptions = node.exceptions;

        if (node.visibleAnnotations != null) {
            this.visibleAnnotations = node.visibleAnnotations
                    .stream()
                    .map(GhostAnnotationNode::of)
                    .collect(Collectors.toList());
        }

        if (node.invisibleAnnotations != null) {
            this.invisibleAnnotations = node.invisibleAnnotations
                    .stream()
                    .map(GhostAnnotationNode::of)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public MethodNode read() {
        final MethodNode methodNode = new MethodNodeBuilder()
                .access(access)
                .name(name)
                .desc(desc)
                .signature(signature)
                .exceptions(exceptions.toArray(new String[0]))
                .build();

        if (visibleAnnotations != null) {
            methodNode.visibleAnnotations = new ArrayList<>();
            for (GhostAnnotationNode visibleAnnotation : visibleAnnotations) {
                methodNode.visibleAnnotations.add(visibleAnnotation.read());
            }
        }

        if (invisibleAnnotations != null) {
            methodNode.invisibleAnnotations = new ArrayList<>();

            for (GhostAnnotationNode invisibleAnnotation : invisibleAnnotations) {
                methodNode.invisibleAnnotations.add(invisibleAnnotation.read());
            }
        }

        return methodNode;
    }

    public static GhostMethodNode of(final MethodNode methodNode) {
        return new GhostMethodNode(methodNode);
    }
}