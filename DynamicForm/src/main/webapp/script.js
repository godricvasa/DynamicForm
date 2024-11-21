function fetchDropDownMetadata() {
  $.ajax({
    url: "DropDownServlet",
    type: "GET",
    success: function (response) {
      response.forEach(function (name) {
        console.log(name.tableName);
        let opt = `<option value="${name.tableName}"> ${name.tableName} </option>`;

        $("#tableSelect").append(opt);
      });
      console.log(response);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      if (jqXHR.status === 401) {
        // window.location.href = "DynamicForm/login";
        window.location.reload();
      }

//      console.log("error fetching dropdown data");
    },
  });
}
function fetchFormMetadata(tableName) {
  $("#dynamicForm").show();
  $.ajax({
    url: "FormMetadataServlet",
    type: "GET",
    data: { tableName: tableName },
    success: function (response) {
      $("#form-fields").empty();
      console.log(response);
      // Dynamically create form fields based on the JSON metadata
      response.forEach(function (field) {
        let inputField = `<div class="form-group">
                                    <label for="${field.label}">${field.label}</label>
                                    <input id="${field.label}" type="${field.data_type}" name="${field.label}" />
                                  </div>`;
        $("#form-fields").append(inputField);
      });
    },
    error: function (jqXHR, textStatus, errorThrown) {
      if (jqXHR.status === 401) {
        // window.location.href = "DynamicForm/login";
        window.location.reload();
      }
      console.log("error fetching data");
    },
  });
}

$(document).ready(function () {
  $("#logout").click(function () {
    $.ajax({
      url: "endSession",
      type: "POST",
      success: function (response) {
        alert("you are logged out");
        window.location.href = response;
      },
      error: function (jqXHR) {
        if (jqXHR.status === 401) {
          //  window.location.href = "DynamicForm/login"
          window.location.reload();
        }
        alert("failed to logout");
      },
    });
  });

  $("#dynamicForm").hide();
  fetchDropDownMetadata();

  // Set interval to check session status every 5 seconds
  // Adjust the interval as needed

  //welcoming the user
  $.ajax({
    url: "getSession",
    type: "GET",
    success: function (response) {
      console.log(response);
      $("#welcome").text("Welcome " + response);
    },
    error: function (jqXHR, textStatus, errorThrown) {
      if (jqXHR.status === 401) {
        // window.location.href = "DynamicForm/login";
        window.location.reload();
      }
      console.log("no user found");
    },
  });
  //before fetching any data well get the username from the session
  //the session closes only when the user logout
  //so we need to do 2 things , one is getting the username from ajax get and during logout the session should get terminated

  // Fetch the initial form data from json meta data

  // Fetch form data when the table selection changes
  $("#tableSelect").on("change", function () {
    if ($("#tableSelect").val() === "select") {
      let name = $("#tableSelect").val() + " table";
      $("#title").text(name);
      $("#dynamicForm").hide();
      return;
    }

    let name = $("#tableSelect").val() + " table";
    $("h1").text(name);

    fetchFormMetadata($("#tableSelect").val());
    fetchFormMetadata($(this).val());
  });

  // Handle form submission
  $("#dynamicForm").on("submit", function (e) {
    let tableName = $("#tableSelect").val();
    e.preventDefault();

    let action = $(document.activeElement).val();
    console.log(action);

    let formData = {
      tableName: $("#tableSelect").val(),
      fields: [],
    };
    if (action === "submit") {
      console.log(tableName);
      $("#dynamicForm")
        .find("input")
        .each(function () {
          let field = {
            label: $(this).attr("name"),
            value: $(this).val(),
          };
          formData.fields.push(field);
          console.log(field);
        });
      console.log(formData);

      // Send the form data to the server
      $.ajax({
        url: "SubmitFormServlet",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(formData),
        success: function (response) {
          alert("Form submitted successfully");
        },
        error: function (jqXHR, textStatus, errorThrown) {
          if (jqXHR.status === 401) {
            window.location.reload();
            // window.location.href = "DynamicForm/login";
          }
          console.log("error submitting data");
        },
      });
      $("#dynamicForm")
        .find("input")
        .each(function () {
          $(this).val("");
        });
    } else if (action === "retrieve") {
      console.log(tableName);

      $.ajax({
        url: "FormDataServlet",
        type: "GET",
        contentType: "application/json",
        data: { tableName: tableName },
        success: function (response) {
          response.forEach(function (field) {
            console.log(field.label);
            console.log(field.value);
            $("#" + field.label).val(field.value);
          });

          console.log(response);
        },
        error: function (jqXHR, textStatus, errorThrown) {
          if (jqXHR.status === 401) {
            window.location.reload();
            // window.location.href = "DynamicForm/login";
          }
          console.log("error fetching data");
        },
      });
    } else if (action === "delete") {
      console.log(tableName);
      $.ajax({
        url: "FormDeleteServlet",
        type: "POST",
        data: { tableName: tableName },
        success: function (response) {
          alert("record deleted successfully");
          $("#dynamicForm")
            .find("input")
            .each(function () {
              $(this).val("");
            });
        },
        error: function (jqXHR, textStatus, errorThrown) {
          if (jqXHR.status === 401) {
            window.location.reload();
            // window.location.href = "DynamicForm/login";
          }
          console.log("error deleting data");
        },
      });
    }
  });
});
