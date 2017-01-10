$('#content .item')

$('#content .item .photo>a')[0].href // ссылка на подробное описание
$('#content .item .photo>a>img')[0].src // картинка

$('#content .item .date > .block0 > .day') // день начала показа
$('#content .item .date > .block0 > .month') // месяц начала показа

$('#content .item .title > a')[0].href // ссылка на подробное описание
$('#content .item .title > a')[0].text // заголовок
$('#content .item .title > .title_orig')[0].text // заголовок (оригинал)

$('#content .item .descr ') // описание
$('#content .item .descr > stromg') // зал
$('#content .item .descr > p') // кто участвовал при создании + краткое описание сюжета

$('#content .item .time ') // Время начала сеансов + возрастное ограничение



var domItems = $('#content .item');

var items = domItems.map(function(i, item){
	var domDate = $(item).find('.date > .block0');
	var descArr = $(item).find('.descr > p').map(function(i, item){ return $(item).html().split('<br>'); });
	
	return {
		image: $(item).find('.photo > a > img').attr('src')
		,start: domDate.find('.day').text() +' '+ domDate.find('.month').text()
		,href: $(item).find('.title > a').attr('href')
		,title: $(item).find('.title > a').text()
		,titleOrigin: $(item).find('.title > .title_orig').text()
		,room: $(item).find('.descr > strong').text()
		,description: $('<div>'+ Array.join(descArr, '\n') +'</div>').text().replace(/[\n\r]/g,'\n')
	};	
});
items = Array.from(items);
console.log('-------------------------');
console.log(JSON.stringify(items));
console.log('-------------------------');
console.log(items);
console.log('-------------------------');
