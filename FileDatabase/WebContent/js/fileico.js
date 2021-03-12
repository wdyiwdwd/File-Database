
$(document).ready(function(){
		
		$(".folder").mouseover(function(){
			$(this).children(".cirlcebutton").show();
			$(this).children(".filename").show();
			$(this).children(".filename_s").hide();
		})
		
		$(".folder").mouseout(function(){
			$(this).children(".cirlcebutton").hide();
			$(this).children(".filename").hide();
			$(this).children(".filename_s").show();
		})
		
		$(".file").mouseover(function(){
			$(this).children(".cirlcebutton").show();
			$(this).children(".filename").show();
			$(this).children(".filename_s").hide();
		})

		$(".file").mouseout(function(){
			$(this).children(".cirlcebutton").hide();
			$(this).children(".filename").hide();
			$(this).children(".filename_s").show();
		})
		
		$("#loading").hide();
		$("#insertDB2").click(function(){
			$("#loading").hide();
		})
		$("#insertDB3").click(function(){
			$("#loading").hide();
		})
		
		$("#SHA256Div").hide();
		
		
		$("#classDiv").change(function() { 
			var selectText = $("#classDiv").find("option:selected").val();
			if(selectText == "2"){
				$("#SHA256Div").show();
			}
			else{
				$("#SHA256Div").hide();
			}
		}); 
		
		//每隔40s刷新一次页面
		//setTimeout("location.reload()",40*1000);

})


function insertSubmit(){
	if(confirm('确认插入新的数据库吗？')){
		$("#insert").hide();
		$("#loading").show();
		return true;
	}
	else{
		return false;
	}
}