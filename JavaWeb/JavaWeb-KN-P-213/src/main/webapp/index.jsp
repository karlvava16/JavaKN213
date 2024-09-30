<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>KN-P-213</title>
</head>
<body>
<h1>Java web. JSP</h1>
<img src="img/Java_Logo.svg" alt="logo" style="height: 200px">

<p>
    JSP - Java Server Pages - технологія веб-розробки з динамічним
    формуванням HTML сторінок. Аналогічно до PHP, ранніх ASP є
    надбудовою над HTML, що розширює його додаючи
</p>
<ul>
    <li>Вирази</li>
    <li>Змінні</Li>
    <li>Алгоритмічні конструкції (умови, цикли)</li>
    <li>Взаємодію з іншими файлами-сторінками</li>
</ul>
<p>
    Основу JSP складають спеціалізовані теги &lt;% %&gt; та &lt;%= %&gt; <br/>
    Тег &lt;% %&gt; включає в себе Java-код, тег &lt;%= %&gt; виводить
    результат (є скороченою формою оператора <code>print()</code>).
</p>
<p>

    Вирази частіше все задаються тегом, що виводить, у якому може бути
    довільний вираз (коректний для Java). Виведення результату здійснюється
    у тому місці, де знаходиться тег: <br/>
    &lt; %= 2 + 3 %&gt; =< %= 2+3%>
</p>
<h2>3мiнні</h2>

<p>

    Змінні, їх оголошення та призначення (без виведення результату)
    оформлюється у блоці &lt;% %&gt;
        <%

String str = "Hello, World!";
double[] prices = { 10.0, 20.0, 30.0, 40.0 };

%>
<pre>

    &lt;%
    String str = "Hello, World!";
    double[] prices = { 10.0, 20.0, 30.0, 40.0 };
    %&gt;
</pre>
<p>

    Виведення значень змінних - знов тег <br/>
    &lt;%= str %&gt; &rarr; <%= str %>
</p>

<h2>Алгоритмічні конструкції</h2>

<% for (int i = 0; i < prices. length; i++) { %>
<i><%= prices[i] %></i>&emsp;
<% } %>
<pre>
    &lt;% for (int i = 0; i < prices. length; i++) { %&gt;
        &lt;i&lt;%= prices[i] %&gt ;< &lt;/i&lt;&amp;emsp;
    &lt;% } %&gt;
</pre>

</body>
</html>
