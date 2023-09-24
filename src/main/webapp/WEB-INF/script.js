// Show the modal
window.handleCreateAccount = () => {
    console.log("Clicked create account");

    const modal  = document.getElementById("create-account-modal");

    modal.classList.remove("hidden");

    // Add click listener for the close button
    const closeModalButton = document.getElementById("closeModalButton");
    closeModalButton.addEventListener("click", () => {
        modal.classList.add("hidden");
    });
};

function handleCreateAccountSubmit() {
    console.log("Submit");
    const password = document.getElementById("create-account-password").value;
    const confirmPassword = document.getElementById("create-account-passwordConfirm").value;

    if(password !== confirmPassword) {
        alert('Passwords do not match');
        return false;
    }

    const modal  = document.getElementById("create-account-modal");

    modal.classList.add("hidden");

    return true;
}

function handleLogin() {
    console.log("Log in");
}

// Add a "checked" symbol when clicking on a task list
var list = document.querySelector('ul');
list.addEventListener('click', function(ev) {
    if(ev.target.tagName === 'LI') {
        var clickedTaskId = document.getElementById(ev.target.id).id;
        console.log(clickedTaskId);

        // put http request to change done status
        fetch('/tasks/?done=' + clickedTaskId, {
            method: 'PUT',
        })
            .then(response => {
            if(response.ok) {
                console.log("Success done");

                // Toggle 'done' class on the li element
                ev.target.classList.toggle('done');
            } else {
                return response.text().then(text => Promise.reject(text));
            }
        })
            .catch(error => {
                console.log('Error: ' + error);
            })
    }
})

// Click on delete button to remove task from current list
var deleteTask = document.getElementsByClassName("delete-task");
for(var i = 0; i < deleteTask.length; i++) {
    deleteTask[i].onclick = function() {
        var liId = this.parentElement.id;

        // delete request with http
        fetch('/tasks/?del=' + liId, {
            method: 'DELETE',
        })
            .then(response => {
                if(response.ok) {
                    console.log("Success delete");
                    // remove li element from DOM
                    var liElement = document.getElementById(liId);
                    if(liElement) liElement.remove();
                } else {
                    return response.text().then(text => Promise.reject(text));
                }
            })
            .catch((error) => {
                console.log('Error: ', error);
            })
    }
}