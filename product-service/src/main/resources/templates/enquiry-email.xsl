<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>

    <!-- Match the root element of the HTML -->
    <xsl:template match="/html">
        <xsl:element name="html">
            <xsl:element name="head">
                <xsl:element name="title">
                    <xsl:text>Email Template</xsl:text>
                </xsl:element>
            </xsl:element>
            <xsl:element name="body">
                <!-- Extract the content from the HTML and place it inside the body -->
                <xsl:apply-templates select="//body/div/*"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!-- Match the div elements -->
    <xsl:template match="div">
        <xsl:element name="div">
            <xsl:attribute name="style">font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;</xsl:attribute>
            <!-- Extract and copy the content of the div element -->
            <xsl:copy-of select="node()"/>
        </xsl:element>
    </xsl:template>

    <!-- Match the h2, p, and other elements -->
    <xsl:template match="h2 | p">
        <!-- Copy the element and its content -->
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- Ignore meta, link, style, and script elements -->
    <xsl:template match="meta | link | style | script"/>

</xsl:stylesheet>
