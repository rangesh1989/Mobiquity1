/**
 * @author-rangesh
 */

$(document).ready(function() {
  $('.wrapper-sales-edit').hide(); //Initially form wil be hidden.
  $('.button-edit').click(function() {
    $('.wrapper-sales-edit').toggle();//Form toggles on button click
  });
});