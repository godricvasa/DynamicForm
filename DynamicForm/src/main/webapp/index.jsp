<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Main Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>

        body {
            font-family: Arial, sans-serif;
        }
        form {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ccc;
            background-color: #f9f9f9;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input {
            width: 90%;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        button {
            padding: 10px 20px;
            background-color: #28a745;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #218838;
        }
      select{
            font-family: 'Times New Roman', Times, serif;

            font-size:15px;
            padding: 10px;
            box-shadow: #ccc 2px;
        }


    </style>
</head>
<body>

    <h1 id="title">select Table</h1>

    <select id="tableSelect" value>
    <option value="select">select</option>
    </select>

    <form id="dynamicForm">
        <div class="form-group" id="form-fields"></div>

        <button type="submit" value="submit">Submit</button>
        <button type="submit" value="retrieve">retrieve</button>
        <button type="submit" value="delete">Delete</button>

    </form>


    <script type="text/javascript"><%@include file="script.js" %></script><div>


</body>
</html>
