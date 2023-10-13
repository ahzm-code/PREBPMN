package fr.blind.anonymous.rbpmn.visualisation;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JFrame;

import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.enforcement.ProbabilisticEnforcer;
import fr.blind.anonymous.rbpmn.file.CSVWriter;
import fr.blind.anonymous.rbpmn.visualisation.RealTimeChart;

public class CreateChart {
	private BPMNProcess bpmnProcess;
	private CSVWriter writer;
	private int period;
	private ProbabilisticEnforcer probEnforcer;
	
	public CreateChart(BPMNProcess bpmnProcess, int period) throws UnsupportedEncodingException, FileNotFoundException{
		this.bpmnProcess = bpmnProcess;
		this.writer = new CSVWriter("probability.csv");
		this.period = period;
		
	}
	
	public CreateChart(BPMNProcess bpmnProcess,ProbabilisticEnforcer probEnforcer, int period) throws UnsupportedEncodingException, FileNotFoundException{
		this.bpmnProcess = bpmnProcess;
		this.writer = new CSVWriter("probability.csv");
		this.period = period;
		this.probEnforcer = probEnforcer;
		
	}
	
	
	public void showProbChart() {
		JFrame frame = new JFrame("Verdict Results");
		//RealTimeChart rtcp = new RealTimeChart("Random Data", "Random", "Value");
		RealTimeChart rtcp = new RealTimeChart("PMC verdict", "Runtime Enforcement", "Probability", this.probEnforcer, this.period);
		rtcp.SetProcessKey(this.bpmnProcess);
		rtcp.setCSVWrtier(this.writer);
		new BorderLayout();
		frame.getContentPane().add(rtcp, BorderLayout.CENTER);
		//frame.setBounds(200, 120, 10, 80);
		//frame.pack();
		frame.setSize(500, 400);
		frame.setVisible(true);
		(new Thread(rtcp)).start();
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowevent) {
				System.exit(0);
			}

		});
	}
}
