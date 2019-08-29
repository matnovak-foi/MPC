togglestate = false;
function toggleExpansion(){
    if (togglestate) {
        a = jQuery('tr.ui-expanded-row');
        a.find('.ui-row-toggler').click();
        togglestate = false;
    } else {
        a=jQuery('tr:not(.ui-expanded-row)');
        a.find('.ui-row-toggler').click();
        togglestate = true;
    }
};

function scrollToMatchPartsSideBySide(idA, idB){
    window.location.href = idA
    window.location.href = idB
}