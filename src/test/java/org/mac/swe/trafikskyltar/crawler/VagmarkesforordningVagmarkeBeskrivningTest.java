package org.mac.swe.trafikskyltar.crawler;

import org.junit.jupiter.api.Test;
import org.mac.swe.trafikskyltar.model.vagmarke.VagmarkesforordningSkyltBeskrivning;

import static org.junit.jupiter.api.Assertions.*;

class VagmarkesforordningVagmarkeBeskrivningTest {

    @Test
    void setCode() {
    }

    @Test
    void setLabel_whenNoWhitespace_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("def456");
        assertEquals( "def456", f.getUnderrubrik());
    }

    @Test
    void setLabel_whenWhitespace_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("  def456");
        assertEquals( "def456", f.getUnderrubrik());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax1_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("TT  def456");
        assertEquals( "TT  def456", f.getUnderrubrik());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax2_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("TX9  def456");
        assertEquals( "TX9  def456", f.getUnderrubrik());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax3_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("TX9def456");
        assertEquals( "TX9def456", f.getUnderrubrik());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax4_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("TX9 def456");
        assertEquals( "TX9 def456", f.getUnderrubrik());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithInvalidSyntax5_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("T9def456");
        assertEquals( "T9def456", f.getUnderrubrik());
        assertEquals( "", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax1_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("T9  def456");
        assertEquals( "def456", f.getUnderrubrik());
        assertEquals( "T9", f.getCode());
    }
    @Test
    void setLabel_whenTrailingCodeWithValidSyntax2_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("T9 def456");
        assertEquals( "def456", f.getUnderrubrik());
        assertEquals( "T9", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax3_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("T9354 def456");
        assertEquals( "def456", f.getUnderrubrik());
        assertEquals( "T9354", f.getCode());
    }

    @Test
    void setLabel_whenTrailingCodeWithValidSyntax4_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setUnderrubrik("T535    def456");
        assertEquals( "def456", f.getUnderrubrik());
        assertEquals( "T535", f.getCode());
    }

    @Test
    void setBeskrivning_whenNoWhitespace_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setText("abc123");
        assertEquals( "abc123", f.getText());
    }

    @Test
    void setBeskrivning_whenWhitespace_expectSet() {
        VagmarkesforordningSkyltBeskrivning f = new VagmarkesforordningSkyltBeskrivning();
        f.setText("  def456");
        assertEquals( "def456", f.getText());
    }
}