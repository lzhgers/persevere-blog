package com.lzh.lzhframework.utils;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.util.List;

/**
 * @author LZH
 * @date 2023/2/18
 */
public class MarkdownUtils {

    public static List<String> getAllImgTagFromMarkdown(String markdown) {
        List<String> imgTags = RegexUtils.match(markdown, "!\\[(.*?)\\]\\((.*?)\\)");
        return imgTags;
    }
    /**
     * Markdown转Html
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();
        Node document = parser.parse(markdown);
        String html = renderer.render(document);
        return html;
    }

    public static List<String> getAllImgTagFromHtml(String html) {
        return RegexUtils.match(html, "<img\\s+(?:[^>]*)src\\s*=\\s*([^>]+)>");
    }
}
