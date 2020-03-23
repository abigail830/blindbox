var inlineTags = {
	abbr: 1,
	b: 1,
	big: 1,
	code: 1,
	del: 1,
	em: 1,
	i: 1,
	ins: 1,
	label: 1,
	q: 1,
	small: 1,
	span: 1,
	strong: 1
}
export default {
	getStyle: function(style) {
		if (style) {
			var i, j, res = '';
			if ((i = style.indexOf("display")) != -1)
				res = style.substring(i, (j = style.indexOf(';', i)) == -1 ? style.length : j);
			if (style.indexOf("flex") != -1) res += ';' + style.match(getRegExp("flex[:-][^;]+/g")).join(';');
			return res;
		} else return '';
	},
	getNode: function(item) {
		return [item];
	},
	useRichText: function(item) {
		if (item.c || inlineTags[item.name] || (item.attrs.style && item.attrs.style.indexOf("display:inline") != -1))
			return false;
		return true;
	}
}
