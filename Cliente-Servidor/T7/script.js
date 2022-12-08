var foto=null
function get(id){
	return document.getElementById(id);
}
function readSingleFile(files,imagen)    
{
    var file = files[0];
    if (!file) return;
    var reader = new FileReader();
    reader.onload = function(e){
        imagen.src = reader.result;
        // reader.result incluye al principio: "data:image/jpeg;base64,"
        foto = reader.result.split(',')[1];
    };
    reader.readAsDataURL(file);
}
function quita_foto(){
    foto=null;
	get('alta_imagen').src='assets/no_image.png';
	get('alta_file').value='';
}