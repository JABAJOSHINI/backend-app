<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Spring Boot Email using FreeMarker</title>
</head>
<body>
<table width="600" style="border:1px solid #333">
  <tr>

    <th>companyName</th>
    <th>FullName</th>
     <th>vessel</th>
     <th>IMO</th>
     <th>Equipment</th>
     <th>make</th>
     <th>modelNumber</th>
     <th>Address</th>
  </tr>
  <tr>
    <td align="center">


        <tr>
          <td> ${model.companyName} </td>
          <td> ${model.fullName} </td>
          <td> ${model.vessel} </td>
          <td> ${model.imo} </td>
          <td> ${model.equipment} </td>
           <td> ${model.make} </td>
           <td> ${model.modelNumber}</td>

           <td> ${model.address} </td>
        </tr>

    </td>
  </tr>
</table>
<div >Description: ${model.description}</div>

<br/>
<div> Have a nice day..!</div>
</body>
</html>