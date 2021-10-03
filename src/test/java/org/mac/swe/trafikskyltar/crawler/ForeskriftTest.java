package org.mac.swe.trafikskyltar.crawler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForeskriftTest {

    @Test
    void setCode() {
    }

    @Test
    void setLabel_whenNoWhitespace_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("def456");
        assertEquals( "def456", f.getLabel());
    }

    @Test
    void setLabel_whenWhitespace_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("  def456");
        assertEquals( "def456", f.getLabel());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax1_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("TT  def456");
        assertEquals( "TT  def456", f.getLabel());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax2_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("TX9  def456");
        assertEquals( "TX9  def456", f.getLabel());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax3_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("TX9def456");
        assertEquals( "TX9def456", f.getLabel());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax4_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("TX9 def456");
        assertEquals( "TX9 def456", f.getLabel());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax5_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("T9def456");
        assertEquals( "T9def456", f.getLabel());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax1_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("T9  def456");
        assertEquals( "def456", f.getLabel());
        assertEquals( "T9", f.getCode());
    }
    @Test
    void setLabel_whenTrailingCodeWithValidSyntax2_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("T9 def456");
        assertEquals( "def456", f.getLabel());
        assertEquals( "T9", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax3_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("T9354 def456");
        assertEquals( "def456", f.getLabel());
        assertEquals( "T9354", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax4_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setLabel("T535    def456");
        assertEquals( "def456", f.getLabel());
        assertEquals( "T535", f.getCode());
    }

    @Test
    void setBeskrivning_whenNoWhitespace_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setBeskrivning("abc123");
        assertEquals( "abc123", f.getBeskrivning());
    }

    @Test
    void setBeskrivning_whenWhitespace_expectSet() {
        Foreskrift f = new Foreskrift();
        f.setBeskrivning("  def456");
        assertEquals( "def456", f.getBeskrivning());
    }
}