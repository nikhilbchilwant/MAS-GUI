package mas.maintenanceproxy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.alee.laf.scroll.WebScrollPane;

import mas.maintenanceproxy.agent.LocalMaintenanceAgent;
import mas.maintenanceproxy.classes.PMaintenance;
import mas.maintenanceproxy.gui.preventive.PrevMaintTableModel;
import mas.maintenanceproxy.gui.preventive.PrevMaintTableRenderer;
import net.miginfocom.swing.MigLayout;

public class MaintenanceGUI extends JFrame{

	private static final long serialVersionUID = 1L;

	private LocalMaintenanceAgent mAgent;

	private JTabbedPane tPanes;
	private String[] tabTitles = {"Maintenance Schedules"};
	private JPanel[] panelsForTab;
	private JScrollPane scroller;

	private JButton btnSendPrevMaint;

	private JPanel containerPanel;
	// time in a week in milliseconds.
	// this is the time between two consecutive preventive maintenance schedules
	private long pmInterval = 604800000L;

	//
	private SpinnerListModel spinnerListModel;
	private SpinnerNumberModel spinnerNumberModel;
	private String[] months;

	private JSpinner jSpinnerMonth;
	private JSpinner jSpinnerYear;

	private CorrectiveMaintenanceFrame correctiveFrame;

	private PMSchedulePanel pmPanel;
	private JTable pmScheduleTable;
	private PrevMaintTableRenderer tableRenderer;
	private PrevMaintTableModel tableModel;
	private WebScrollPane maintScroller;

	private CalendarPanel calendarPanel;
	private JPanel schedulePanel;
	private JPanel jpNorth;

	private int dayInWeek;
	private long pmDuration;
	private int hourDay;
	private int minuteDay;

	private ArrayList<PMaintenance> prevMaintSchedules;

	public MaintenanceGUI(LocalMaintenanceAgent lmAgent) {

		this.mAgent = lmAgent;

		prevMaintSchedules = new ArrayList<PMaintenance>();
		pmPanel = new PMSchedulePanel();

		containerPanel = new JPanel(new BorderLayout());
		schedulePanel = new JPanel(new BorderLayout());
		this.tPanes = new JTabbedPane(JTabbedPane.TOP);
		panelsForTab = new JPanel[tabTitles.length];

		initTablePanel();
		//		initCalenderPane();

		for (int i = 0, n = tabTitles.length; i < n; i++ ) {
			panelsForTab[i] = new JPanel(new MigLayout());
		}
		schedulePanel.add(containerPanel,BorderLayout.NORTH);
		schedulePanel.add(maintScroller,BorderLayout.CENTER);
		//		schedulePanel.add(calendarPanel);

		panelsForTab[0].add(schedulePanel);

		for (int i = 0, n = tabTitles.length; i < n; i++) {
			this.tPanes.addTab(tabTitles[i],panelsForTab[i] );
		}

		add(this.tPanes);
		showGui();
	}

	private void initTablePanel() {
		tableRenderer = new PrevMaintTableRenderer();
		tableModel = new PrevMaintTableModel();
		this.pmScheduleTable = new JTable(tableModel);

		pmScheduleTable.setDefaultRenderer(PrevMaintTableRenderer.class, tableRenderer);
		pmScheduleTable.setDefaultEditor(PrevMaintTableRenderer.class, tableRenderer);
		pmScheduleTable.setRowHeight(110);

		maintScroller = new WebScrollPane(pmScheduleTable);
		maintScroller.setPreferredWidth(350);
		
		containerPanel.add(pmPanel, BorderLayout.CENTER);
	}

	public static void unloadPrevMaintSchedule() {
		//		completedJobListinfoPanel.removeAll();
		//		MainPanel.remove(completedJobListinfoPanel);
		//		welcomeScreenFrame.validate();
		//		welcomeScreenFrame.repaint();
		//		welcomeScreenFrame.setVisible(true);
	}

	public void showMaintenanceStartNotification() {

	}

	private void initCalenderPane() {
		months = new DateFormatSymbols().getMonths();
		spinnerListModel = new SpinnerListModel(Arrays.asList(months).subList(0, 12));
		spinnerNumberModel = new SpinnerNumberModel(2012, 0, 3000, 1);

		jSpinnerMonth = new JSpinner(spinnerListModel);
		jSpinnerYear = new JSpinner(spinnerNumberModel);

		calendarPanel = new CalendarPanel();

		jpNorth = new JPanel(new GridLayout(1, 2));
		jpNorth.add(jSpinnerMonth);
		jpNorth.add(jSpinnerYear);

		calendarPanel.setMonth(spinnerListModel.
				getList().indexOf(spinnerListModel.getValue()));

		calendarPanel.setYear(((Integer)spinnerNumberModel.
				getValue()).intValue());

		JSpinner.NumberEditor numberEditor = new JSpinner.NumberEditor(jSpinnerYear, "####");
		jSpinnerYear.setEditor(numberEditor);

		jSpinnerMonth.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				calendarPanel.setMonth(spinnerListModel.getList().indexOf(spinnerListModel.getValue()));
			}
		});

		jSpinnerYear.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				calendarPanel.setYear(((Integer)spinnerNumberModel.getValue()).intValue());
			}
		});
	}

	public void showRepairTimeInput() {
		this.setEnabled(false);
		correctiveFrame = new CorrectiveMaintenanceFrame(mAgent,this);
	}

	private void showGui() {
		setTitle(" Local Maintenance Agent : " );//+ mAgent.getLocalName().split("#")[1]);
		setPreferredSize(new Dimension(800,600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int centerX = (int)screenSize.getWidth() / 2;
		int centerY = (int)screenSize.getHeight() / 2;
		setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
		super.setVisible(true);
	}

	public static void createPrevMaintPanel(PMaintenance pMaintTileCell) {

	}

}
