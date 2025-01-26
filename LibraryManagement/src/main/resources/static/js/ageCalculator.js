function calculateAge(birthday, deathDate = null) {
    if (!birthday) {
        return;
    }

    // Konvertimi i datës së lindjes në objekt Date
    var birthDate = new Date(birthday);
    var currentDate = new Date();

    // Nëse ka një datë vdekjeje, llogarisim diferencën mes dy datave
    if (deathDate) {
        var deathDateObj = new Date(deathDate);
        return calculateYears(deathDateObj, birthDate);
    } else {
        // Nëse nuk ka datë vdekjeje, llogarisim moshën aktuale
        return calculateYears(currentDate, birthDate);
    }
}

function calculateYears(date1, date2) {
    var years = date1.getFullYear() - date2.getFullYear();
    var m = date1.getMonth() - date2.getMonth();

    if (m < 0 || (m === 0 && date1.getDate() < date2.getDate())) {
        years--;
    }

    return years;
}
