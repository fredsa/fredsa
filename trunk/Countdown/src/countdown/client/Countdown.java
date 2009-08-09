package countdown.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import java.util.Date;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Countdown implements EntryPoint {
  double size = 80;

  public void onModuleLoad() {
    final HTML label = new HTML();
    RootPanel.get().add(label);
    //    label.getElement().getStyle().setMargin(0.7, Unit.EM);
    Date endDate = new Date();
    endDate.setYear(2009 - 1900);
    endDate.setMonth(8 - 1);
    endDate.setDate(9);
    endDate.setHours(16);
    endDate.setMinutes(0);
    endDate.setSeconds(0);
    final long end = endDate.getTime();

    label.getElement().getStyle().setFontSize(++size, Unit.PT);
    label.addClickHandler(new ClickHandler() {

      public void onClick(ClickEvent event) {
        if (event.getNativeEvent().getButton() == NativeEvent.BUTTON_LEFT) {
          label.getElement().getStyle().setFontSize(++size, Unit.PT);
        } else {
          label.getElement().getStyle().setFontSize(--size, Unit.PT);
        }
      }
    });

    Timer timer = new Timer() {

      @Override
      public void run() {
        long now = System.currentTimeMillis();
        long diff = end - now;
        String text = "<br>";
        if (diff <= 0) {
          text += "-";
          diff = -diff;
          label.getElement().getStyle().setColor("red");
        }
        diff /= 1000;
        int hours = (int) (diff / 3600);
        diff -= hours * 3600;
        int minutes = (int) (diff / 60);
        diff -= minutes * 60;
        int seconds = (int) diff;
        text += format(hours) + "h " + format(minutes) + "m "
            + format(seconds)
            + "s";
        label.setHTML(text);
      }

      private String format(int seconds) {
        return seconds < 10 ? "0" + seconds : "" + seconds;
      }
    };
    timer.scheduleRepeating(1000);
  }
}
