function handleClientLoad() {
  gapi.load('client:auth2', initClient);
}

function initClient() {
  console.log('initClient');
}
