function copyToClipboard() {
    navigator.clipboard.writeText($('#past').val());
    $('#copier').attr('data-bs-toggle', 'tooltip').attr('title', 'Copied!');
    $("[id='copier']").tooltip();
    $('#copier').blur().focus();
}

function changeExpiryUnit() {
    if ($('#expiryUnit').val() === 'NEVER') {
        $('#expiryValue').attr("disabled", true);
    } else {
        $('#expiryValue').removeAttr("disabled");
    }
}

function changePasswordUsage() {
    if ($('#usePassword').is(':checked')) {
        $('#password').removeAttr("disabled");
    } else {
        $('#password').attr("disabled", true);
    }
}