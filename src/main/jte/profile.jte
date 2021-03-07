@param bolbolestan.student.StudentEntity student

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
    <style>
        li {
            padding: 5px
        }
        table{
            width: 10%;
            text-align: center;
        }
    </style>
</head>
<body>
<ul>
    <li id="std_id">Student Id: ${student.getStudentId()}</li>
    <li id="first_name">First Name: ${student.getName()}</li>
    <li id="last_name">Last Name: ${student.getSecondName()}</li>
    <li id="birthdate">Birthdate: ${student.getBirthDate()}</li>
    <li id="gpa">GPA: 17.33</li>
    <li id="tpu">Total Passed Units: 70</li>
</ul>
<table>
    <tr>
        <th>Code</th>
        <th>Grade</th>
    </tr>
    <tr>
        <td>8101001</td>
        <td>18</td>
    </tr>
    <tr>
        <td>8101002</td>
        <td>16</td>
    </tr>
    <tr>
        <td>8101002</td>
        <td>14</td>
    </tr>
</table>
</body>
</html>
