package com.ge.research.sadl.darpa.aske.tests

import org.eclipse.xtext.diagnostics.Severity
import org.junit.Test

import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

class DialogTest extends AbstractDialogTest {
	
	@Test
	def void dummy_test() {
		'''
			uri  "http://sadl.org/Test.sadl" alias Test.
			
			Rock is
			
			Person is a class described by age with values of type int.
			ChildrenList is a type of Person List length 1-*.
			children describes Person with values of type ChildrenList.
			children of Person has at most 1 value.
			
			PersonList is a type of Person List.
			PersonListList is a type of PersonList List.
			
			foo describes Person with values of type PersonListList.
			bar describes Person with values of type Person List length 1-4.
			bar of Person only has values of type Person List.
			bar of Person only has values of type Person List length 1-4.
			bar of Person has at least one value of type Person List length 1-4.
			bar of Person has at least 1 value of type Person List length 1-4.
			bar of Person has at most 2 value of type Person List length 1-4.
			
			
			Rule R1: if x is a Person and
					x has bar y and 
					y is a Person List //length 1-4
					z is a Person List length 1-4
			then print("Hurray!"). //x has age 50.
		'''.sadl
		
		'''
		uri "http://baseUri".
		import "http://sadl.org/SpeedOfSound.sadl".
		
		
		CM: "Parsing code file 'C:/Users/200005201/sadl3-master6/git/DARPA-ASKE-TA1/Ontology/M5/ExtractedModels/Sources/Mach.java'".
		CM: "The following methods doing computation were found in the extraction:".
		CM: External Mach.CAL_TT(double T, double M, double G, double Q) returns double: "http://com.ge.research.sadl.darpa.aske.answer/Mach_java#Mach.CAL_TT".
		
		CM: Mach.CAL_TT has expression (a Script with language Java, with script 
		"public double CAL_TT(double T, double M, double G, double Q) {
		    double TT = T / TQTT(M, G);
		    double EPS = 0.00001;
		    double Z = 1;
		    double Z2 = 0;
		    double TT2 = 0;
		    double TT1;
		    double Z1;
		    double i = 1;
		    while (Math.abs(Z) > EPS) {
		        Z = Math.pow(M, 2) - 2 * TT / CAL_GAM(T, G, Q) / T * (G / (G - 1) * (1 - T / TT) + Q / TT * (1 / (Math.exp(Q / TT) - 1) - 1 / (Math.exp(Q / T) - 1)));
		        if (i == 1) {
		            Z2 = Z;
		            TT2 = TT;
		            TT = TT2 + 100;
		            i = 2;
		        } else {
		            TT1 = TT2;
		            Z1 = Z2;
		            TT2 = TT;
		            Z2 = Z;
		            TT = TT2 - Z2 * (TT2 - TT1) / (Z2 - Z1);
		        }
		    }
		    if (M <= .1)
		        TT = T / TQTT(M, G);
		    return TT;
		}"
		), has expression (a Script with language Python, with script 
		"#!/usr/bin/env python
		\"\"\" generated source for module inputfile \"\"\"
		class Mach(object):
		    \"\"\" generated source for class Mach \"\"\"
		    def CAL_TT(self, T, M, G, Q):
		        \"\"\" generated source for method CAL_TT \"\"\"
		        TT = T / TQTT(M, G)
		        EPS = 0.00001
		        Z = 1
		        Z2 = 0
		        TT2 = 0
		        TT1 = float()
		        Z1 = float()
		        i = 1
		        while Math.abs(Z) > EPS:
		            Z = Math.pow(M, 2) - 2 * TT / CAL_GAM(T, G, Q) / T * (G / (G - 1) * (1 - T / TT) + Q / TT * (1 / (Math.exp(Q / TT) - 1) - 1 / (Math.exp(Q / T) - 1)))
		            if i == 1:
		                Z2 = Z
		                TT2 = TT
		                TT = TT2 + 100
		                i = 2
		            else:
		                TT1 = TT2
		                Z1 = Z2
		                TT2 = TT
		                Z2 = Z
		                TT = TT2 - Z2 * (TT2 - TT1) / (Z2 - Z1)
		        if M <= 0.1:
		            TT = T / TQTT(M, G)
		        return TT
		
		").
		CM: External Mach.CAL_SOS(double T, double G, double R, double Q) returns double: "http://com.ge.research.sadl.darpa.aske.answer/Mach_java#Mach.CAL_SOS".
		
		CM: Mach.CAL_SOS has expression (a Script with language Java, with script 
		"public double CAL_SOS(double T, double G, double R, double Q) {
		    double WOW = 1 + (G - 1) / (1 + (G - 1) * Math.pow((Q / T), 2) * Math.exp(Q / T) / Math.pow((Math.exp(Q / T) - 1), 2));
		    return (Math.sqrt(32.174 * T * R * WOW));
		}"
		), has expression (a Script with language Python, with script 
		"#!/usr/bin/env python
		\"\"\" generated source for module inputfile \"\"\"
		class Mach(object):
		    \"\"\" generated source for class Mach \"\"\"
		    def CAL_SOS(self, T, G, R, Q):
		        \"\"\" generated source for method CAL_SOS \"\"\"
		        WOW = 1 + (G - 1) / (1 + (G - 1) * Math.pow((Q / T), 2) * Math.exp(Q / T) / Math.pow((Math.exp(Q / T) - 1), 2))
		        return (Math.sqrt(32.174 * T * R * WOW))
		
		").
		CM: External Mach.CAL_T(double TT, double M, double G, double Q) returns double: "http://com.ge.research.sadl.darpa.aske.answer/Mach_java#Mach.CAL_T".
		
		CM: Mach.CAL_T has expression (a Script with language Java, with script 
		"double CAL_T(double TT, double M, double G, double Q) {
		    double T = TT * TQTT(M, G);
		    double EPS = 0.00001;
		    double Z = 1;
		    double Z2 = 0;
		    double T2 = 0;
		    double T1;
		    double Z1;
		    double i = 1;
		    while (Math.abs(Z) > EPS) {
		        Z = Math.pow(M, 2) - 2 * TT / CAL_GAM(T, G, Q) / T * (G / (G - 1) * (1 - T / TT) + Q / TT * (1 / (Math.exp(Q / TT) - 1) - 1 / (Math.exp(Q / T) - 1)));
		        if (i == 1) {
		            Z2 = Z;
		            T2 = T;
		            T = T2 + 100;
		            i = 2;
		        } else {
		            T1 = T2;
		            Z1 = Z2;
		            T2 = T;
		            Z2 = Z;
		            T = T2 - Z2 * (T2 - T1) / (Z2 - Z1);
		        }
		    }
		    if (M <= .1)
		        T = TT * TQTT(M, G);
		    return T;
		}"
		), has expression (a Script with language Python, with script 
		"#!/usr/bin/env python
		\"\"\" generated source for module inputfile \"\"\"
		class Mach(object):
		    \"\"\" generated source for class Mach \"\"\"
		    def CAL_T(self, TT, M, G, Q):
		        \"\"\" generated source for method CAL_T \"\"\"
		        T = TT * TQTT(M, G)
		        EPS = 0.00001
		        Z = 1
		        Z2 = 0
		        T2 = 0
		        T1 = float()
		        Z1 = float()
		        i = 1
		        while Math.abs(Z) > EPS:
		            Z = Math.pow(M, 2) - 2 * TT / CAL_GAM(T, G, Q) / T * (G / (G - 1) * (1 - T / TT) + Q / TT * (1 / (Math.exp(Q / TT) - 1) - 1 / (Math.exp(Q / T) - 1)))
		            if i == 1:
		                Z2 = Z
		                T2 = T
		                T = T2 + 100
		                i = 2
		            else:
		                T1 = T2
		                Z1 = Z2
		                T2 = T
		                Z2 = Z
		                T = T2 - Z2 * (T2 - T1) / (Z2 - Z1)
		        if M <= 0.1:
		            T = TT * TQTT(M, G)
		        return T
		
		").
		CM: External Mach.CAL_GAM(double T, double G, double Q) returns double: "http://com.ge.research.sadl.darpa.aske.answer/Mach_java#Mach.CAL_GAM".
		
		CM: Mach.CAL_GAM has expression (a Script with language Java, with script 
		"public double CAL_GAM(double T, double G, double Q) {
		    return (1 + (G - 1) / (1 + (G - 1) * (Math.pow((Q / T), 2) * Math.exp(Q / T) / Math.pow((Math.exp(Q / T) - 1), 2))));
		}"
		), has expression (a Script with language Python, with script 
		"#!/usr/bin/env python
		\"\"\" generated source for module inputfile \"\"\"
		class Mach(object):
		    \"\"\" generated source for class Mach \"\"\"
		    def CAL_GAM(self, T, G, Q):
		        \"\"\" generated source for method CAL_GAM \"\"\"
		        return (1 + (G - 1) / (1 + (G - 1) * (Math.pow((Q / T), 2) * Math.exp(Q / T) / Math.pow((Math.exp(Q / T) - 1), 2))))
		
		").
		CM: External Mach.TQTT(double M, double G) returns double: "http://com.ge.research.sadl.darpa.aske.answer/Mach_java#Mach.TQTT".
		
		CM: Mach.TQTT has expression (a Script with language Java, with script 
		"public double TQTT(double M, double G) {
		    return Math.pow((1 + (G - 1) / 2 * Math.pow(M, 2)), -1);
		}"
		), has expression (a Script with language Python, with script 
		"#!/usr/bin/env python
		\"\"\" generated source for module inputfile \"\"\"
		class Mach(object):
		    \"\"\" generated source for class Mach \"\"\"
		    def TQTT(self, M, G):
		        \"\"\" generated source for method TQTT \"\"\"
		        return Math.pow((1 + (G - 1) / 2 * Math.pow(M, 2)), -1)
		
		").
		CM: "Would you like to save the extracted model (Mach.java.owl) in SADL format"?
		
		Build CAL_SOS.
		CM: "Failed: Unable to find model 'http://sadl.org/SpeedOfSound.sadl#CAL_SOS' in code model".
		'''.assertValidatesTo [ ontModel, issues, processor |
			assertNotNull(ontModel)
			assertTrue(issues.filter[severity === Severity.ERROR].empty)
		]
	}
	
}