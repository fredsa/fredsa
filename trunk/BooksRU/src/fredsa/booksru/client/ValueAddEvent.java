package fredsa.booksru.client;

import com.google.gwt.event.shared.GwtEvent;

public class ValueAddEvent<I> extends GwtEvent<ValueAddHandler<I>> {

  /**
   * Handler type.
   */
  private static Type<ValueAddHandler<?>> TYPE;

  /**
   * Fires a value change event on all registered handlers in the handler
   * manager.If no such handlers exist, this method will do nothing.
   * 
   * @param <I> the old value type
   * @param source the source of the handlers
   * @param value the value
   */
  public static <I> void fire(HasValueAddHandlers<I> source, I value) {
    if (TYPE != null) {
      ValueAddEvent<I> event = new ValueAddEvent<I>(value);
      source.fireEvent(event);
    }
  }

  /**
   * Fires value change event if the old value is not equal to the new value.
   * Use this call rather than making the decision to short circuit yourself for
   * safe handling of null.
   * 
   * @param <I> the old value type
   * @param source the source of the handlers
   * @param oldValue the oldValue, may be null
   * @param newValue the newValue, may be null
   */
  public static <I> void fireIfNotEqual(HasValueAddHandlers<I> source, I oldValue, I newValue) {
    if (shouldFire(source, oldValue, newValue)) {
      ValueAddEvent<I> event = new ValueAddEvent<I>(newValue);
      source.fireEvent(event);
    }
  }

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ValueAddHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<ValueAddHandler<?>>();
    }
    return TYPE;
  }

  /**
   * Convenience method to allow subtypes to know when they should fire a value
   * change event in a null-safe manner.
   * 
   * @param <I> value type
   * @param source the source
   * @param oldValue the old value
   * @param newValue the new value
   * @return whether the event should be fired
   */
  protected static <I> boolean shouldFire(HasValueAddHandlers<I> source, I oldValue, I newValue) {
    return TYPE != null && oldValue != newValue && (oldValue == null || !oldValue.equals(newValue));
  }

  private final I value;

  /**
   * Creates a value change event.
   * 
   * @param value the value
   */
  protected ValueAddEvent(I value) {
    this.value = value;
  }

  // The instance knows its BeforeSelectionHandler is of type I, but the TYPE
  // field itself does not, so we have to do an unsafe cast here.
  @SuppressWarnings("unchecked")
  @Override
  public final Type<ValueAddHandler<I>> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Gets the value.
   * 
   * @return the value
   */
  public I getValue() {
    return value;
  }

  @Override
  public String toDebugString() {
    return super.toDebugString() + getValue();
  }

  @Override
  protected void dispatch(ValueAddHandler<I> handler) {
    handler.onValueAdd(this);
  }
}
