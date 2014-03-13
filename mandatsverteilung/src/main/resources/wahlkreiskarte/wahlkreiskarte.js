'use strict';

var dk = {};
(function(dk, L, _, $) {

	var map = {
		// Karte und Wahlkreis-Daten
		mapLayers: [],
		districtData: [],
		info: null,
		
		// Initialisieren
		init: function() {
			this.leafletMap = L.map('map', {
				center: [51.165691, 10.451526],
				zoom: 6,
				minZoom: 5,
				maxZoom: 12,
				attributionControl: false,
			});
			this.initInfo();
			$(dk).on('map.loaded.mapLayers map.loaded.districtData', _.bind(this.completedLoading, this));
			$(dk).on('map.ready', _.bind(this.colorizeLayers, this));
			this.loadMapLayers();
			this.loadDistrictData();
		},
		
		// Karte einfärben
		colorizeLayers: function() {
			_.each(this.mapLayers, _.bind(function(layer) {
				var district = _.find(this.districtData, function(currentDistrict) {
					return currentDistrict.key == layer.key;
				});
				var color = '#f0f0f0';
				if (district) {
					color = this.getColor(district);
				}
				layer.value.setStyle({
					'fillOpacity': 0.65,
					'fillColor': color,
					'color': '#aaaaaa',
				});
			}, this));
		},
		
		// Werte in Farben konvertieren
		getColor: function(district) {
			var color = $.urlParam(district.key);
			if (color === 0) {
				color = 'f0f0f0';
			}
			return "#" + color;
		},
		
		// Karte laden
		loadMapLayers: function() {
			// async laden von file:/// urls geht in Chrome nicht so ohne weiteres, deshalb direkt über js-files
			this.addMapLayers(_wahlkreise);
			$(dk).triggerHandler('map.loaded.mapLayers');
			/*
			$.getJSON('data/wahlkreise.geojson', _.bind(function(data) {
				this.addMapLayers(data);
				$(dk).triggerHandler('map.loaded.mapLayers');
			}, this));
			*/
		},
		addMapLayers: function(geojson) {
			L.geoJson(geojson.features, {
				style: {
					'opacity': 0.5,
					'weight': 1
				},
				onEachFeature: _.bind(this.addMapLayer, this)
			}).addTo(this.leafletMap);
		},
		addMapLayer: function(feature, layer) {
			layer.on({
				mouseover: _.bind(this.highlightFeature, this),
				mouseout: _.bind(this.resetHighlight, this),
				click: _.bind(this.zoomToFeature, this)
			});
			this.mapLayers.push({
				'key': feature.properties['WKR_NR'],
				'label': feature.properties['WKR_NAME'],
				'value': layer
			});
		},
		
		// Interactivity
		initInfo: function() {
			var info = L.control();
			info.onAdd = function (map) {
			    this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
			    this.update();
			    return this._div;
			};
			info.update = function (props) {
				if (props) {
					$(this._div).show();
					this._div.innerHTML = '<h4>' + props.WKR_NAME + " [" + props.WKR_NR + ']</h4>';
				} else {
					$(this._div).hide();
				}
				/*
				this._div.innerHTML = '<h4>US Population Density</h4>' +  (props ?
					'<b>' + props.WKR_NR + '</b><br />' + props.WKR_NAME + ' people / mi<sup>2</sup>'
			        : 'Hover over a state');
				*/
			};
			this.info = info;
			info.addTo(this.leafletMap);
		},
		zoomToFeature: function(e) {
			this.leafletMap.fitBounds(e.target.getBounds());
		},
		highlightFeature: function(e) {
			var layer = e.target;
			this.info.update(layer.feature.properties);
		},
		resetHighlight: function(e) {
		    this.info.update();
		},
		
		// Wahlkreis-Daten laden
		loadDistrictData: function() {
			// async laden von file:/// urls geht in Chrome nicht so ohne weiteres, deshalb direkt über js-files
			this.districtData = _districtData;
			$(dk).triggerHandler('map.loaded.districtData');
			/*
			$.getJSON('data/data.json', _.bind(function(data) {
				this.districtData = data;
				$(dk).triggerHandler('map.loaded.districtData');
			}, this))
			*/
		},
		
		// Karte und/oder Daten geladen
		completedLoading: function() {
			if (!_.isEmpty(this.mapLayers) && !_.isEmpty(this.districtData)) {
				$(dk).triggerHandler('map.ready');
			}
		}
	};
	dk.map = map;
})(dk, L, _, jQuery);

jQuery.urlParam = function(name){
    var results = new RegExp('[\\?&amp;]' + name + '=([^&amp;#]*)').exec(window.location.href);
    return (results && results[1]) || 0;
}

dk.map.init();
