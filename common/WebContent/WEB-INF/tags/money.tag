<%@tag import="java.text.DecimalFormat"%>
<%@attribute name="val" required="true" type="java.lang.Integer" description="金额" %>
<%
	DecimalFormat fmt = new DecimalFormat("###,###,###,###,##0.00");
	out.print(fmt.format(val/100.0));
%>