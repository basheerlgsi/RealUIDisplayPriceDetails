package auctionsniper;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.*;//assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import junit.framework.TestCase;

import auctionsniper.SnipersTableModel;
import auctionsniper.Column;
import org.jmock.integration.junit4.JMock;

@RunWith(JMock.class)
public class SnipersTableModelTest extends TestCase {
	private final Mockery context = new Mockery();
	private TableModelListener listener = context.mock(TableModelListener.class);
	private final SnipersTableModel model = new SnipersTableModel();
	
	@Before 
	public void attachModelListener() { //1
		model.addTableModelListener(listener);
	}
	
	@Test 
	public void hasEnoughColumns() { //2
		assertThat(model.getColumnCount(), equalTo(Column.values().length));
	}
	
	@Test 
	public void setsUpColumnHeadings() {
		for (Column column: Column.values()) {
			assertEquals(column.name, model.getColumnName(column.ordinal()));
			}
	}
	
	@Test 
	public void setsSniperValuesInColumns() {
		context.checking(new Expectations() {{
		one(listener).tableChanged(with(aRowChangedEvent())); //3
	}});
		
		model.sniperStatusChanged(new SniperSnapShot("item id", 555, 666, SniperState.BIDDING));// 4
			
		assertColumnEquals(Column.ITEM_IDENTIFIER, "item id"); //5
		assertColumnEquals(Column.LAST_PRICE, 555);
		assertColumnEquals(Column.LAST_BID, 666);
		assertColumnEquals(Column.SNIPER_STATE, SnipersTableModel.textFor(SniperState.BIDDING));
		
	}
	
	private void assertColumnEquals(Column column, Object expected) {
		final int rowIndex = 0;
		final int columnIndex = column.ordinal();
		assertEquals(expected, model.getValueAt(rowIndex, columnIndex));
	}
	
	private Matcher<TableModelEvent> aRowChangedEvent() { //6
		return samePropertyValuesAs(new TableModelEvent(model, 0));
	}
}
