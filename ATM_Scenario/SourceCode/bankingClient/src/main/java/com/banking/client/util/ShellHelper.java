package com.banking.client.util;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Value;

public class ShellHelper {

  @Value("${shell.out.info}")
  public String infoColor;

  @Value("${shell.out.success}")
  public String successColor;

  @Value("${shell.out.error}")
  public String errorColor;

  private Terminal terminal;

  public ShellHelper(Terminal terminal) {
    this.terminal = terminal;
  }

  /**
   * Construct colored message in the given color.
   *
   * @param message message to return
   * @param color color to print
   * @return colored message
   */
  public String getColored(String message, PromptColor color) {
    return (new AttributedStringBuilder())
        .append(message, AttributedStyle.DEFAULT.foreground(color.toJlineAttributedStyle()))
        .toAnsi();
  }

  /**
   * Print message to the console in the success color.
   *
   * @param message message to print
   */
  public void printSuccess(String message) {
    print(message, PromptColor.valueOf(successColor));
  }

  /**
   * Print message to the console in the info color.
   *
   * @param message message to print
   */
  public void printInfo(String message) {
    print(message, PromptColor.valueOf(infoColor));
  }

  /**
   * Print message to the console in the error color.
   *
   * @param message message to print
   */
  public void printError(String message) {
    print(message, PromptColor.valueOf(errorColor));
  }

  /**
   * Generic Print to the console method.
   *
   * @param message message to print
   * @param color (optional) prompt color
   */
  public void print(String message, PromptColor color) {
    String toPrint = message;
    if (color != null) {
      toPrint = getColored(message, color);
    }
    terminal.writer().println(toPrint);
    terminal.flush();
  }

  // --- set / get methods ---------------------------------------------------

  public Terminal getTerminal() {
    return terminal;
  }

  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }
}
