window.onload = () => {

    getFiles()

}

function getFiles() {

    fetch(`/application`, {

        method: 'GET'

    }).then(response => response.json())
        .then(data => document.getElementById("fileList").innerHTML = data.map(filename =>
            `<option value="${filename}">${filename}</option>`).join(''));

}

function readLines() {

    let progress = 0;
    let percentage = 0;
    const file = document.getElementById("fileList").value;

    fetch(`/application/${file}/lines`, {

        method: 'GET'

    }).then(response => response.json())
        .then(data => {

            function updateProgressBar(progress) {

                progressBar.innerHTML = `<div id='loading-bar' style='width: ${progress}%'></div>`;

            }

            const keys = Object.keys(data)
            let html = `<tr class="show-table-row"><th class="show-table-header">Word</th>
                                <th class="show-table-header">Occurrence</th></tr>`;

            const progressBar = document.getElementById("progress-bar");
            progressBar.innerHTML = "<div id='loading-bar' style='width: 0'></div>";


            keys.forEach(key => {

                progress += 1;
                const length = Object.keys(data).length;
                percentage = (progress / length) * 100;

                updateProgressBar(percentage);

                html += `<tr class="show-table-row"><td class="show-table-entry">${key}</td>
                         <td class="show-table-entry">${data[key]}</td></tr>`

            })

            document.getElementById("showTable").innerHTML = html;

            percentage = 0;
            progress = 0;

        });

}

function abortProgress() {

    window.stop();

}