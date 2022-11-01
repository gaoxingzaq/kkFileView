function getQueryVariable(variable)
{
    var query = window.location.search.substring(1);
    var vars = query.split("&");
    for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        if(pair[0] == variable){return pair[1];}
    }
    return(false);
}
let url = getQueryVariable('file')
    url =decodeURIComponent(url)
  // console.log(url)
	urlToBlob(url);
	function urlToBlob(url) {
      let xhr = new XMLHttpRequest();
      xhr.open("get", url, true);
      xhr.responseType = "blob";
	  let _this = this;
	  var  fileType = url.substring(url.lastIndexOf('/') + 1);
	  var  fileTypet = url.substring(url.lastIndexOf('.') + 1);
	  var  type = 'image/jpeg';
	//  console.log(fileTypet);
	  if (fileTypet.toLowerCase() =="svg"){
		   type = 'image/svg+xml'; 
	  }else if(fileTypet.toLowerCase() =="ai"){
		   type = 'application/postscript';  
	  }else if(fileTypet.toLowerCase() =="pdf"){
		   type = 'application/pdf';  
	  }else if(fileTypet.toLowerCase() =="png"){
		   type = 'application/png';  
	  }
      xhr.onreadystatechange = function () {
	  if (xhr.readyState==4){    
       if (xhr.status == 200 ) {
      var myFile = new File([this.response], fileType, {type: type, lastModified: Date.now()});
nerirong(myFile);

        } }
      };
      xhr.send();
	   }
var nerirong1;
function nerirong(neirong){
nerirong1 = neirong;
//console.log(nerirong1);
 return nerirong1;
}


(window.webpackJsonp = window.webpackJsonp || [])
.push([
	[14], {
		105: function(e, t, o) {
			"use strict";
			o.r(t);
			class r {
				constructor() {
					this.isClsSVG = !1, this.activated = !1
				}
				get renderer() {
					if (this._renderer) return this._renderer;
					throw new Error("Renderer is not inited")
				}
				setRenderer(e) {
					this.activated ? this.deactivate()
						.then(() => {
							if (this._renderer = e, this._renderer) return this.activate()
						}) : this._renderer = e
				}
				activate() {
					return this._renderer ? this.bindRenderer()
						.then(() => {
							this.activated = !0
						}) : Promise.reject(new Error("Renederer is not set. cannot activate"))
				}
				deactivate() {
					return this.unbindRenderer()
						.then(() => {
							this.activated = !1
						})
				}
			}
			var s = o(0);
			class a extends r {
				constructor() {
					super(...arguments), this.name = "tool_hand_name", this.slug = "tool_hand", this.cls = "fas fa-hand-paper", this.mouseDown = !1, this.onMouseDown = e => {
						if (e) {
							this.mouseDown = !0;
							const t = e.e;
							this.lastPoint = {
								x: t.clientX,
								y: t.clientY
							}, s.b.dispatch({
								type: "toolStateHandPanStart"
							}), t.stopPropagation(), t.preventDefault()
						}
					}, this.onMouseMove = e => {
						if (this.mouseDown && e) {
							const t = e.e,
								o = {
									x: t.clientX,
									y: t.clientY
								},
								r = this.renderer;
							this.lastPoint && (r.panX(r.panX() - o.x + this.lastPoint.x), r.panY(r.panY() - o.y + this.lastPoint.y)), this.lastPoint = o, t.stopPropagation(), t.preventDefault()
						}
					}, this.onMouseUpAndLeave = e => {
						if (this.mouseDown = !1, this.lastPoint = void 0, s.b.dispatch({
							type: "toolStateHandPanEnd"
						}), e) {
							const t = e.e;
							t.stopPropagation(), t.preventDefault()
						}
					}
				}
				async bindRenderer() {
					const e = this.renderer;
					e.on("mousedown", this.onMouseDown), e.on("mousemove", this.onMouseMove), e.on("mouseup", this.onMouseUpAndLeave), e.on("mouseleave", this.onMouseUpAndLeave)
				}
				async unbindRenderer() {
					const e = this.renderer;
					e.off("mousedown", this.onMouseDown), e.off("mousemove", this.onMouseMove), e.off("mouseup", this.onMouseUpAndLeave), e.off("mouseleave", this.onMouseUpAndLeave)
				}
			}
			var i = o(131),
				n = o(202),
				c = o(6);
			class l extends r {
				constructor() {
					super(...arguments), this.name = "tool_inspect_name", this.slug = "tool_inspect", this.cls = "fas fa-mouse-pointer", this.isClsSVG = !1, this.hoverLayers = [], this.onMouseDown = e => {
						const t = this.hoverLayers[0];
						if (t && t !== this.compositeChoseLayer) {
							const o = e.e;
							o.metaKey || o.shiftKey || o.ctrlKey ? (Object(c.a)("choose_layer_canvas_multi"), s.b.dispatch({
								type: "toolStateToggleChoseLayer",
								layers: [t]
							})) : (Object(c.a)("choose_layer_canvas_single"), s.b.dispatch({
								type: "toolStateChoseLayer",
								layers: [t]
							}))
						}
						this.hoverLayers.length > 0 && s.b.dispatch({
							type: "toolStateNeabyLayers",
							layers: this.hoverLayers.concat([])
						})
					}, this.onMouseMove = async e => {
						if (e) {
							const t = this.renderer.rendererPointToRealPoint(this.renderer.mouseEventToCoords(e), !1),
								o = this.renderer.pageByRealCoords(t);
							if (o) {
								const e = this.renderer.realPointToPagePoint(t, o),
									r = (await Object(n.a)(e, await o.getLayers()))
									.map(e => e.layer);
								this.renderer.clearDrawing(this.hoverLayerGroup), this.hoverLayers = r, this.drawHoverLayer(), this.prepareDrawMeasure()
							} else this.renderer.clearDrawing(this.hoverLayerGroup), this.renderer.clearDrawing(this.measureLinesGroup), this.hoverLayers = []
						}
					}, this.refreshTimer = void 0, this.refreshDrawing = () => {
						this.refreshTimer && clearTimeout(this.refreshTimer), this.refreshTimer = setTimeout(() => {
							this.refreshTimer = void 0, this.renderer.clearDrawing(this.hoverLayerGroup), this.drawHoverLayer(), this.drawCompositeLayer(), this.prepareDrawMeasure()
						}, 50)
					}
				}
				get choseLayers() {
					return s.b.getState()
						.toolState.choseLayers
				}
				get compositeChoseLayer() {
					return Object(i.b)(this.choseLayers)
				}
				get hoverLayer() {
					return this.hoverLayers[0]
				}
				get zoomLevel() {
					return s.b.getState()
						.canvasState.zoomLevel
				}
				get metricScale() {
					return s.b.getState()
						.canvasState.metricScale
				}
				prepareDrawMeasure() {
					this.renderer.clearDrawing(this.measureLinesGroup), this.compositeChoseLayer && this.hoverLayer && this.compositeChoseLayer !== this.hoverLayer && this.compositeChoseLayer.page === this.hoverLayer.page && this.drawMeasurement(this.compositeChoseLayer, this.hoverLayer)
				}
				drawMeasurement(e, t) {
					const o = e.rect.panTo(this.renderer.pagePointToRealPoint(e.rect.leftTop, e.page))
						.zoom(this.zoomLevel),
						r = t.rect.panTo(this.renderer.pagePointToRealPoint(t.rect.leftTop, e.page))
						.zoom(this.zoomLevel),
						s = o.distance(r);
					for (const e in s)
						if (s.hasOwnProperty(e)) {
							const t = s[e];
							if (0 === t) continue;
							const a = e[0],
								i = e[1],
								n = Math.round(t / this.metricScale / this.zoomLevel);
							switch (a) {
								case "l":
								case "r":
									this.drawVLineMeasurements("l" === a ? o.left : o.right, o.top, o.bottom, "l" === i ? r.left : r.right, r.top, r.bottom, o.left > r.left, n.toString());
									break;
								case "t":
								case "b":
									this.drawHLineMeasurements("t" === a ? o.top : o.bottom, o.left, o.right, "t" === i ? r.top : r.bottom, r.left, r.right, o.top > r.top, n.toString())
							}
						}
				}
				drawMeasureLineOnRenderer(e, t, o) {
					this.renderer.draw({
						...o,
						x1: e.x,
						y1: e.y,
						x2: t.x,
						y2: t.y
					}, this.measureLinesGroup)
				}
				drawMeasureLabelOnRenderer(e) {
					e = {
						...e,
						textBackgroundFill: "black",
						fillColor: "white",
						fontSize: 14,
						fontWeight: "bold"
					};
					var t = this.renderer.measureText(e.txt, e);
					e.top -= t.height / 2, e.left -= t.width / 2, this.renderer.draw(e, this.measureLinesGroup)
				}
				drawVLineMeasurements(e, t, o, r, s, a, i, n) {
					const c = {
							strokeColor: "orange",
							strokeDashArray: "5 5",
							strokeWidth: 1
						},
						l = {
							strokeColor: "orange",
							strokeWidth: 1
						};
					let p = Math.min(t, o, s, a),
						u = Math.max(t, o, s, a);
					if (e === r) return void this.drawMeasureLineOnRenderer({
						x: e,
						y: p
					}, {
						x: e,
						y: u
					}, c);
					if (i) {
						const i = r;
						r = e, e = i, p = s, s = t, t = p, u = a, a = o, o = u
					}
					t > a && this.drawMeasureLineOnRenderer({
						x: e,
						y: t
					}, {
						x: e,
						y: s
					}, c), o < s && this.drawMeasureLineOnRenderer({
						x: e,
						y: o
					}, {
						x: e,
						y: a
					}, c);
					const h = d(t, o, s, a);
					let b = 0;
					b = h ? Math.round((h.s + h.e) / 2) : Math.round((s + a) / 2), this.drawMeasureLineOnRenderer({
						x: e,
						y: b
					}, {
						x: r,
						y: b
					}, l), this.drawMeasureLabelOnRenderer({
						left: (e + r) / 2,
						top: b,
						txt: n
					})
				}
				drawHLineMeasurements(e, t, o, r, s, a, i, n) {
					const c = {
							strokeColor: "orange",
							strokeDashArray: "5 5",
							strokeWidth: 1
						},
						l = {
							strokeColor: "orange",
							strokeWidth: 1
						};
					let p = Math.min(t, o, s, a),
						u = Math.max(t, o, s, a);
					if (e === r) return void this.drawMeasureLineOnRenderer({
						x: e,
						y: p
					}, {
						x: e,
						y: u
					}, c);
					if (i) {
						const i = r;
						r = e, e = i, p = s, s = t, t = p, u = a, a = o, o = u
					}
					t > a && this.drawMeasureLineOnRenderer({
						x: t,
						y: e
					}, {
						x: s,
						y: e
					}, c), o < s && this.drawMeasureLineOnRenderer({
						x: o,
						y: e
					}, {
						x: a,
						y: e
					}, c);
					const h = d(t, o, s, a);
					let b = 0;
					b = h ? Math.round((h.s + h.e) / 2) : Math.round((s + a) / 2), this.drawMeasureLineOnRenderer({
						x: b,
						y: e
					}, {
						x: b,
						y: r
					}, l), this.drawMeasureLabelOnRenderer({
						left: b,
						top: (e + r) / 2,
						txt: n
					})
				}
				drawLayer(e, t) {
					if (t) {
						const {
							layer: o,
							rect: r,
							txt: s
						} = t, a = t.layer.page, i = this.zoomLevel, n = this.renderer.pagePointToRealPoint({
							x: o.rect.left,
							y: o.rect.top
						}, a), c = o.rect.zoom(1 / this.metricScale), l = `  ${Math.round(c.width)} x ${Math.round(c.height)}    `, d = {
							...r,
							left: n.x * i,
							top: n.y * i,
							width: o.rect.width * i,
							height: o.rect.height * i
						}, p = {
							...t.txt,
							fontWeight: "bold",
							left: n.x * i,
							top: n.y * i,
							textBackgroundFill: r.strokeColor,
							txt: l
						};
						this.renderer.draw(d, e), this.renderer.draw(p, e)
					} else this.renderer.clearDrawing(e)
				}
				drawCompositeLayer() {
					this.drawLayer(this.choseLayerGroup), this.compositeChoseLayer && this.drawLayer(this.choseLayerGroup, {
						layer: this.compositeChoseLayer,
						rect: {
							fillColor: "rgba(112,0,0,1)".replace("1)", "0.2)"),
							strokeColor: "rgba(112,0,0,1)"
						},
						txt: {
							fontSize: 14,
							fillColor: "white"
						}
					})
				}
				drawHoverLayer() {
					this.drawLayer(this.hoverLayerGroup), this.hoverLayer && this.drawLayer(this.hoverLayerGroup, {
						layer: this.hoverLayer,
						rect: {
							fillColor: "rgba(0, 68, 37,1)".replace("1)", "0.2)"),
							strokeColor: "rgba(0, 68, 37,1)"
						},
						txt: {
							fontSize: 14,
							fillColor: "white"
						}
					})
				}
				async bindRenderer() {
					const e = this.renderer;
					e.on("mousemove", this.onMouseMove), e.on("click", this.onMouseDown), this.hoverLayerGroup = e.getDrawableGroup(), this.choseLayerGroup = e.getDrawableGroup(), this.measureLinesGroup = e.getDrawableGroup(), this.unsubscribe = Object(s.c)([Object(s.a)(e => e.toolState.choseLayers, this.refreshDrawing), Object(s.a)(e => e.canvasState.metricScale, this.refreshDrawing), Object(s.a)(e => e.canvasState.zoomLevel, this.refreshDrawing)])
				}
				async unbindRenderer() {
					const e = this.renderer;
					e.off("click", this.onMouseDown), e.off("mousemove", this.onMouseMove), e.removeDrawableGroup(this.hoverLayerGroup), e.removeDrawableGroup(this.choseLayerGroup), e.removeDrawableGroup(this.measureLinesGroup), this.hoverLayerGroup = void 0, this.choseLayerGroup = void 0, this.measureLinesGroup = void 0, this.unsubscribe && this.unsubscribe()
				}
			}

			function d(e, t, o, r) {
				const s = Math.max(e, o),
					a = Math.min(t, r);
				return a >= s ? {
					s: s,
					e: a
				} : null
			}
			var p = o(1),
				u = o(31),
				h = o(54);
			class b extends r {
				constructor() {
					super(...arguments), this.name = "tool_color_name", this.slug = "tool_color", this.cls = "fas fa-eye-dropper", this.inited = !1, this.onMouseDown = e => {
						this.hoverColor && (s.b.dispatch({
							type: "toolStateColorPick",
							color: this.hoverColor
						}), Object(h.copyColor)(this.hoverColor))
					}, this.onMouseMove = async e => {
						if (e) {
							const t = this.renderer.rendererPointToRealPoint(this.renderer.mouseEventToCoords(e), !1),
								o = this.renderer.pageByRealCoords(t);
							if (o) {
								if (this.curPageContext && this.curPageContext.page !== o && (this.curPageContext = void 0), !this.curPageContext) {
									const e = await o.getPreview(),
										t = Object(u.imgToCanvas)(e),
										r = e;
									this.curPageContext = {
										page: o,
										magImg: r,
										pageCanvas: t
									}
								}
								const e = this.renderer.realPointToPagePoint(t, o),
									r = this.curPageContext.pageCanvas.getContext("2d")
									.getImageData(e.x, e.y, 1, 1);
								this.drawColor(r.data[0], r.data[1], r.data[2], e, o), this.hoverColor = `rgb(${r.data[0]}, ${r.data[1]},${r.data[2]})`, s.b.dispatch({
									type: "toolStateColorHover",
									color: this.hoverColor
								})
							} else this.hoverColor = void 0
						}
					}
				}
				get zoomLevel() {
					return s.b.getState()
						.canvasState.zoomLevel
				}
				async drawColor(e, t, o, r, s) {
					const a = new p.Rect(Math.ceil(r.x - 100 / 18), Math.ceil(r.y - 100 / 18), Math.ceil(r.x + 100 / 18), Math.ceil(r.y + 100 / 18)),
						i = await Object(u.canvasToImgUrl)(Object(u.scaleCanvas)(Object(u.cropCanvas)(this.curPageContext.pageCanvas, a), 18, !1), !0),
						n = a.zoom(18),
						c = this.renderer.pagePointToRealPoint(r, s),
						l = c.x * this.zoomLevel,
						d = c.y * this.zoomLevel,
						h = {
							circle: {
								radius: 100,
								left: l + 23,
								top: d + 23
							}
						};
					this.inited ? (this.renderer.updateDraw(this.colorBand, {
						radius: 123,
						left: l + 0,
						top: d + 0,
						fillColor: `rgb(${e},${t},${o})`
					}), this.renderer.updateDraw(this.roundEdge, {
						radius: 103,
						left: l + 20,
						top: d + 20,
						fillColor: "#d1d1d1"
					}), this.renderer.updateDraw(this.magImg, {
						img: {
							src: i,
							width: n.width,
							height: n.height
						},
						left: l + 23,
						top: d + 23,
						clip: h
					})) : (this.colorBand = this.renderer.draw({
						radius: 123,
						left: l + 0,
						top: d + 0,
						fillColor: `rgb(${e},${t},${o})`
					}, this.colorGroup), this.roundEdge = this.renderer.draw({
						radius: 103,
						left: l + 20,
						top: d + 20,
						fillColor: "#d1d1d1"
					}, this.colorGroup), this.magImg = this.renderer.draw({
						img: {
							src: i,
							width: n.width,
							height: n.height
						},
						left: l + 23,
						top: d + 23,
						clip: h
					}, this.colorGroup), this.inited = !0)
				}
				async bindRenderer() {
					const e = this.renderer;
					this.colorGroup = e.getDrawableGroup(), e.on("mousemove", this.onMouseMove), e.on("click", this.onMouseDown)
				}
				async unbindRenderer() {
					const e = this.renderer;
					e.off("click", this.onMouseDown), e.off("mousemove", this.onMouseMove), e.removeDrawableGroup(this.colorGroup), this.inited = !1
				}
			}

			function m(e) {
				return f()
					.find(t => t instanceof e)
			}

			function f() {
				let e = [];
				return Object.keys(g)
					.forEach(t => {
						e = e.concat(g[t])
					}), e
			}
			o.d(t, "getToolInst", (function() {
				return m
			})), o.d(t, "tools", (function() {
				return g
			})), o.d(t, "setActiveTool", (function() {
				return y
			})), o.d(t, "toggleTool", (function() {
				return v
			})), o.d(t, "ColorTool", (function() {
				return b
			})), o.d(t, "HandTool", (function() {
				return a
			})), o.d(t, "InspectTool", (function() {
				return l
			})), o.d(t, "BaseTool", (function() {
				return r
			}));
			const g = {
				inspect: [new l, new b],
				general: [new a],
				prototype: []
			};
			async function y(e) {
				const t = s.b.getState()
					.toolState.curTool;
				t !== e && (t && await t.deactivate(), e && await e.activate(), s.b.dispatch({
					type: "toolStateChooseTool",
					tool: e
				}))
			}
			async function v(e) {
				e.activated ? await e.deactivate() : await e.activate()
			}
			Object(s.a)(e => e.canvasState.renderer, e => {
				if (f()
					.forEach(t => {
						t.setRenderer(e)
					}), e) {
					y(g.inspect[0]);
					const e = m(a);
					e && v(e)
				}
			})
		},
		131: function(e, t, o) {
			"use strict";
			o.d(t, "a", (function() {
				return s
			})), o.d(t, "b", (function() {
				return a
			}));
			var r = o(1);

			function s(e, t = "CompositeLayer") {
				if (0 === e.length) return null;
				return {
					layers: e,
					name: t,
					rect: e.reduce((e, t) => e.valid && t.rect.valid ? e.combine(t.rect) : e.valid ? e.clone() : t.rect.clone(), e[0].rect.clone()),
					page: e[0].page,
					layerType: r.LayerType.composite,
					visible: !0,
					style: {}
				}
			}

			function a(e, t = "CompositeLayer") {
				return 1 === e.length ? e[0] : 0 === e.length ? null : s(e)
			}
		},
		202: function(e, t, o) {
			"use strict";
			o.d(t, "a", (function() {
				return s
			})), o.d(t, "b", (function() {
				return a
			}));
			var r = o(36);
			async function s(e, t, o = !0) {
				let a = [];
				const {
					x: i,
					y: n
				} = e;
				for (let o = t.length - 1; o >= 0; o--) {
					const c = t[o];
					if (c.visible && (!c.rect.valid || c.rect.containsCoords(i, n)))
						if (Object(r.b)(c)) {
							if (c.childrenLength > 0) {
								const t = await s(e, await c.children(), !1);
								t.length > 0 && (a = t.concat(a), a.push({
									layer: c,
									dist: c.rect.coordsToCenter({
										x: i,
										y: n
									})
								}))
							}
						} else c.rect.valid && c.rect.containsCoords(i, n) && a.push({
							layer: c,
							dist: c.rect.coordsToCenter({
								x: i,
								y: n
							})
						})
				}
				return a.sort((e, t) => e.dist - t.dist), o && (a = a.slice(0, 7)), a
			}
			async function a(e, t) {
				if (e === t) return !0;
				let o = t.parent;
				for (; o;) {
					if (o === e) return !0;
					o = o.parent
				}
				return !1
			}
		},
		36: function(e, t, o) {
			"use strict";

			function r(e) {
				return void 0 !== e.getPixelImg
			}

			function s(e) {
				return void 0 !== e.getSvgString
			}

			function a(e) {
				return void 0 !== e.getText
			}

			function i(e) {
				return void 0 !== e.children
			}

			function n(e) {
				return void 0 !== e.layers
			}

			function c(e) {
				return i(e) && s(e)
			}
			o.d(t, "d", (function() {
				return r
			})), o.d(t, "f", (function() {
				return s
			})), o.d(t, "e", (function() {
				return a
			})), o.d(t, "b", (function() {
				return i
			})), o.d(t, "a", (function() {
				return n
			})), o.d(t, "c", (function() {
				return c
			}))
		},
		455: function(e, t, o) {
			var r = o(456);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		456: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".inspectStudio{flex-grow:1;flex-shrink:1;position:relative;display:flex}.inspectStudio>.mainContainer{display:flex;position:absolute;top:0;bottom:0;left:0;right:0}.inspectStudio>.mainContainer .flexColumn{flex-direction:column}.inspectStudio>.mainContainer .flexItem{flex:1}.inspectStudio>.mainContainer .main{order:2;border-left:1px solid #404a4b;border-right:1px solid #404a4b;display:flex;position:relative}.inspectStudio>.mainContainer .main .ad{position:absolute;left:0;right:0;top:0;z-index:9999}.inspectStudio>.mainContainer .main .canvasWrapper{flex:1 1 auto;position:relative}.inspectStudio>.mainContainer .sidebarLeft{order:1}.inspectStudio>.mainContainer .sidebarRight{order:3}.inspectStudio>.fileLoaderWrapper .formatsWrapper{padding:20px;border-radius:10px;background:#282f2f;position:absolute;top:20px;right:20px;z-index:5}.inspectStudio>.fileLoaderWrapper .fileDropper{position:absolute;top:0;bottom:0;left:0;right:0}.inspectStudio>.loading{margin-left:auto;margin-right:auto;align-self:center;display:flex;align-items:center}.inspectStudio>.loading>i{margin-right:10px;color:#2ecc71}\n", ""])
		},
		457: function(e, t, o) {
			var r = o(458);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		458: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".canvas{position:absolute;left:0;right:0;top:0;bottom:0;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.canvas .tools{position:absolute;bottom:20px;right:20px;background:rgba(0,0,0,0.6);z-index:10;display:flex;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;align-items:stretch;border-radius:8px}.canvas .tools>*{padding:4px;padding-top:3px;padding-bottom:3px}.canvas .tools .text{font-size:0.8em;font-weight:bold;margin-right:5px}.canvas .tools .zoomTool{display:flex;align-items:center}.canvas .tools .zoomTool .zoom{margin-left:8px;margin-right:8px;cursor:pointer;font-size:1.2em}.canvas .tools .metricScale{background:rgba(255,255,255,0.05);cursor:pointer}.canvas .tools .metricScale .metrics{padding-left:3px}.canvas .tools .metricScale .selectable:hover{background:#104ecb}\n", ""])
		},
		459: function(e, t, o) {
			(function(o) {
				var r, s, a;
				s = [], void 0 === (a = "function" == typeof(r = function() {
					"use strict";

					function t(e, t, o) {
						var r = new XMLHttpRequest;
						r.open("GET", e), r.responseType = "blob", r.onload = function() {
							i(r.response, t, o)
						}, r.onerror = function() {
							console.error("could not download file")
						}, r.send()
					}

					function r(e) {
						var t = new XMLHttpRequest;
						return t.open("HEAD", e, !1), t.send(), 200 <= t.status && 299 >= t.status
					}

					function s(e) {
						try {
							e.dispatchEvent(new MouseEvent("click"))
						} catch (o) {
							var t = document.createEvent("MouseEvents");
							t.initMouseEvent("click", !0, !0, window, 0, 0, 0, 80, 20, !1, !1, !1, !1, 0, null), e.dispatchEvent(t)
						}
					}
					var a = "object" == typeof window && window.window === window ? window : "object" == typeof self && self.self === self ? self : "object" == typeof o && o.global === o ? o : void 0,
						i = a.saveAs || ("object" != typeof window || window !== a ? function() {} : "download" in HTMLAnchorElement.prototype ? function(e, o, i) {
							var n = a.URL || a.webkitURL,
								c = document.createElement("a");
							o = o || e.name || "download", c.download = o, c.rel = "noopener", "string" == typeof e ? (c.href = e, c.origin === location.origin ? s(c) : r(c.href) ? t(e, o, i) : s(c, c.target = "_blank")) : (c.href = n.createObjectURL(e), setTimeout((function() {
								n.revokeObjectURL(c.href)
							}), 4e4), setTimeout((function() {
								s(c)
							}), 0))
						} : "msSaveOrOpenBlob" in navigator ? function(e, o, a) {
							if (o = o || e.name || "download", "string" != typeof e) navigator.msSaveOrOpenBlob(function(e, t) {
								return void 0 === t ? t = {
									autoBom: !1
								} : "object" != typeof t && (console.warn("Deprecated: Expected third argument to be a object"), t = {
									autoBom: !t
								}), t.autoBom && /^\s*(?:text\/\S*|application\/xml|\S*\/\S*\+xml)\s*;.*charset\s*=\s*utf-8/i.test(e.type) ? new Blob(["\ufeff", e], {
									type: e.type
								}) : e
							}(e, a), o);
							else if (r(e)) t(e, o, a);
							else {
								var i = document.createElement("a");
								i.href = e, i.target = "_blank", setTimeout((function() {
									s(i)
								}))
							}
						} : function(e, o, r, s) {
							if ((s = s || open("", "_blank")) && (s.document.title = s.document.body.innerText = "downloading..."), "string" == typeof e) return t(e, o, r);
							var i = "application/octet-stream" === e.type,
								n = /constructor/i.test(a.HTMLElement) || a.safari,
								c = /CriOS\/[\d]+/.test(navigator.userAgent);
							if ((c || i && n) && "object" == typeof FileReader) {
								var l = new FileReader;
								l.onloadend = function() {
									var e = l.result;
									e = c ? e : e.replace(/^data:[^;]*;/, "data:attachment/file;"), s ? s.location.href = e : location = e, s = null
								}, l.readAsDataURL(e)
							} else {
								var d = a.URL || a.webkitURL,
									p = d.createObjectURL(e);
								s ? s.location = p : location.href = p, s = null, setTimeout((function() {
									d.revokeObjectURL(p)
								}), 4e4)
							}
						});
					a.saveAs = i.saveAs = i, e.exports = i
				}) ? r.apply(t, s) : r) || (e.exports = a)
			})
			.call(this, o(13))
		},
		460: function(e, t, o) {
			"use strict";
			var r = r || {};
			r.toast = function() {
				const e = {
					SHOW: {
						"-webkit-transition": "opacity 400ms, -webkit-transform 400ms",
						transition: "opacity 400ms, transform 400ms",
						opacity: "1",
						"-webkit-transform": "translateY(-100%) translateZ(0)",
						transform: "translateY(-100%) translateZ(0)"
					},
					HIDE: {
						opacity: "0",
						"-webkit-transform": "translateY(150%) translateZ(0)",
						transform: "translateY(150%) translateZ(0)"
					}
				};

				function t(o, s, a) {
					if (null !== r()) t.prototype.toastQueue.push({
						text: o,
						options: s,
						transitions: a
					});
					else {
						t.prototype.Transitions = a || e;
						var i = s || {};
						i = t.prototype.mergeOptions(t.prototype.DEFAULT_SETTINGS, i), t.prototype.show(o, i), i = null
					}
				}
				var o = null;

				function r() {
					return o
				}

				function s(e) {
					o = e
				}
				return t.prototype.DEFAULT_SETTINGS = {
					style: {
						main: {
							background: "rgba(0, 0, 0, .85)",
							"box-shadow": "0 0 10px rgba(0, 0, 0, .8)",
							"border-radius": "3px",
							"z-index": "99999",
							color: "rgba(255, 255, 255, .9)",
							padding: "10px 15px",
							"max-width": "60%",
							width: "100%",
							"word-break": "keep-all",
							margin: "0 auto",
							"text-align": "center",
							position: "fixed",
							left: "0",
							right: "0",
							bottom: "0",
							"-webkit-transform": "translateY(150%) translateZ(0)",
							transform: "translateY(150%) translateZ(0)",
							"-webkit-filter": "blur(0)",
							opacity: "0"
						}
					},
					settings: {
						duration: 1500
					}
				}, t.prototype.Transitions = {}, t.prototype.toastQueue = [], t.prototype.timeout = null, t.prototype.mergeOptions = function(e, o) {
					var r = o;
					for (var s in e) r.hasOwnProperty(s) ? null !== e[s] && e[s].constructor === Object && (r[s] = t.prototype.mergeOptions(e[s], r[s])) : r[s] = e[s];
					return r
				}, t.prototype.generate = function(e, o) {
					var a = document.createElement("div");
					"string" == typeof e && (e = document.createTextNode(e)), a.appendChild(e), s(a), a = null, t.prototype.stylize(r(), o)
				}, t.prototype.stylize = function(e, t) {
					Object.keys(t)
						.forEach((function(o) {
							e.style[o] = t[o]
						}))
				}, t.prototype.show = function(e, o) {
					this.generate(e, o.style.main);
					var s = r();
					document.body.insertBefore(s, document.body.firstChild), s.offsetHeight, t.prototype.stylize(s, t.prototype.Transitions.SHOW), s = null, clearTimeout(t.prototype.timeout), t.prototype.timeout = setTimeout(t.prototype.hide, o.settings.duration)
				}, t.prototype.hide = function() {
					var e = r();
					t.prototype.stylize(e, t.prototype.Transitions.HIDE), clearTimeout(t.prototype.timeout), e.addEventListener("transitionend", t.prototype.animationListener), e = null
				}, t.prototype.animationListener = function() {
					r()
						.removeEventListener("transitionend", t.prototype.animationListener), t.prototype.destroy.call(this)
				}, t.prototype.destroy = function() {
					var e = r();
					if (document.body.removeChild(e), e = null, s(null), t.prototype.toastQueue.length > 0) {
						var o = t.prototype.toastQueue.shift();
						t(o.text, o.options, o.transitions), o = null
					}
				}, {
					Toast: t
				}
			}(), e.exports = r.toast
		},
		461: function(e, t, o) {
			var r = o(462);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		462: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".sidebarRight{overflow-y:auto;overflow-x:hidden;width:300px}.sidebarRight .card{border-radius:0}.sidebarRight .card .title{font-size:1em;font-weight:bold;padding:3px;padding-top:5px;padding-bottom:5px;margin:0}\n", ""])
		},
		463: function(e, t, o) {
			var r = o(464);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		464: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, "", ""])
		},
		465: function(e, t, o) {
			var r = o(466);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		466: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".layerBasicView .title{display:flex}.layerBasicView .title .showCss{padding-right:8px;font-size:0.8em;font-weight:bold;cursor:pointer;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.layerBasicView .cardBody{padding-top:10px;padding-bottom:10px;font-size:0.8em}.layerBasicView .cardBody table{width:100%}.layerBasicView .cardBody table tr td{border-bottom:1px solid #aaa;padding-top:5px;padding-bottom:5px}.layerBasicView .cardBody table tr td:first-child{padding-right:1em;font-weight:bold;text-align:right;width:50px;padding-left:1em}.layerBasicView .cardBody table tr td:last-child{padding-left:1em;font-size:1.2em;color:white;cursor:pointer}\n", ""])
		},
		467: function(e, t, o) {
			var r = o(468);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		468: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".clickCopyDiv{position:relative}.clickCopyDiv:hover .label{background:#2064EE;color:white}.clickCopyDiv .copied{position:absolute;padding:3px;background:black;border:1px solid #999;left:30px;top:-10px;color:#fff}\n", ""])
		},
		469: function(e, t, o) {
			var r = o(470);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		470: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".layerExportView .cardBody .formats{display:flex;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.layerExportView .cardBody .formats>span{flex:1 1 0;padding-top:10px;padding-bottom:10px;text-align:center}.layerExportView .cardBody .formats>span.active{background:#2064EE}.layerExportView .cardBody .imgParam .preview{position:relative;height:200px}.layerExportView .cardBody .imgParam .preview .mask{position:absolute;left:0;right:0;bottom:0;top:0;z-index:1;background:rgba(0,0,0,0.8);opacity:0;transition:opacity ease 500ms}.layerExportView .cardBody .imgParam .preview:hover .mask{opacity:1}.layerExportView .cardBody .imgParam .preview>img{max-width:90%;max-height:90%;position:absolute;left:50%;top:50%;-webkit-transform:translate(-50%, -50%);transform:translate(-50%, -50%);z-index:10}.layerExportView .cardBody .imgParam .preview .size{z-index:15;position:absolute;right:0;top:0;padding:3px;padding-left:10px;padding-right:10px;font-size:1em;color:white;background:rgba(0,0,0,0.5)}.layerExportView .cardBody .imgParam .options{display:flex;flex-wrap:wrap}.layerExportView .cardBody .imgParam .options>*{width:50%;border-bottom:1px solid #8c9b9d}.layerExportView .cardBody .imgParam .options>*:nth-child(even){border-left:1px solid #8c9b9d}.layerExportView .cardBody .imgParam .options .dropdown-content{background:#000}.layerExportView .cardBody .row{display:flex;align-items:center}.layerExportView .cardBody .row>div{padding:8px;flex:1 0 auto}.layerExportView .cardBody .fileExport{padding:10px}.layerExportView .cardBody .fileExport .fileName{font-weight:bold;font-size:0.85em;color:#d9d9d9}.layerExportView .cardBody .fileExport .exporters{display:flex;justify-content:space-around;margin-top:10px;margin-bottom:10px}.layerExportView .cardBody .fileExport .exporters>button>i{margin-right:5px}\n", ""])
		},
		471: function(e, t, o) {
			var r = o(472);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		472: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".squareButton{position:relative;display:flex;align-items:center;justify-content:center;padding-top:15px;padding-bottom:15px;background:#282f2f;color:white;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;width:100%;cursor:pointer}.squareButton .head{position:absolute;top:5px;left:5px;font-size:0.8em;font-weight:bold}.squareButton .value{font-size:1em}.squareButton.active{background:#2064EE;color:white}\n", ""])
		},
		473: function(e, t, o) {
			var r = o(474);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		474: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".layerItemContainer{display:flex;border-bottom:1px solid #666;cursor:pointer}.layerItemContainer .layerItem{padding:8px;color:#efefef;display:flex;align-items:center;font-size:0.9em;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;flex-shrink:1;flex-grow:1;overflow:hidden}.layerItemContainer .layerItem.active{background-color:#0c3c9c}.layerItemContainer .layerItem:hover{background-color:#4f85f2}.layerItemContainer .layerItem.invisible{color:#444}.layerItemContainer .layerItem .icon{margin-right:5px;height:16px}.layerItemContainer .layerItem .icon .material-icons{font-size:16px}.layerItemContainer .layerItem .indent{flex-shrink:0}.layerItemContainer .layerItem .name{flex-shrink:1;flex-grow:1;min-width:0;white-space:nowrap;overflow:hidden;text-overflow:ellipsis}.layerItemContainer .layerItem .hint{flex-shrink:0;font-size:0.8em;color:#ebebeb}\n", ""])
		},
		475: function(e, t, o) {
			var r = o(476);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		476: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".nearByLayers .page-head{padding:8px;padding-left:18px;background:#0c3c9c;cursor:default;text-align:center;display:block}\n", ""])
		},
		477: function(e, t, o) {
			var r = o(478);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		478: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".colorPickedView .cardBody{padding:15px}.colorPickedView .cardBody .hoverColor{display:flex;align-items:center;padding-bottom:15px;border-bottom:1px solid #aaa}.colorPickedView .cardBody .hoverColor .preview{padding:25px;margin-right:20px;border:1px solid #aaa;border-radius:5px}.colorPickedView .cardBody .hoverColor .txt{display:flex;flex-direction:column}.colorPickedView .cardBody .hoverColor .txt>span{font-size:0.8em}.colorPickedView .cardBody .hoverColor .txt .hex{font-weight:bold;font-size:1.2rem}.colorPickedView .cardBody .pickedColors{margin-top:15px;display:flex;flex-wrap:wrap}.colorPickedView .cardBody .pickedColors .pickedColor{padding:10px;display:flex;flex-direction:column;align-items:center;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;cursor:pointer}.colorPickedView .cardBody .pickedColors .pickedColor:hover{background:#404a4b}.colorPickedView .cardBody .pickedColors .pickedColor .preview{padding:15px;margin-bottom:8px;border-radius:5px}.colorPickedView .cardBody .pickedColors .pickedColor>span{font-size:0.85rem}\n", ""])
		},
		479: function(e, t, o) {
			var r = o(480);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		480: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".toolbar{display:flex;flex-direction:column;background:#404a4b}\n", ""])
		},
		481: function(e, t, o) {
			var r = o(482);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		482: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".toolBtn{padding-top:1rem;padding-bottom:1rem;padding-left:1rem;padding-right:1rem;font-size:1.5rem;display:flex;align-items:center;justify-content:center;cursor:pointer;color:#8c9b9d}.toolBtn:hover{color:white}.toolBtn.active{color:#2ecc71;background:#2d3435}.toolBtn.toggle.toolLayers.active{background:#282f2f}\n", ""])
		},
		483: function(e, t, o) {
			var r = o(484);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		484: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".sidebarLeft{display:flex}\n", ""])
		},
		485: function(e, t, o) {
			var r = o(486);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		486: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".pageList{width:300px;background:#282f2f;border-right:1px solid white;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;overflow-y:auto}.pageList .page-head{background:#1d2122;font-size:1.1em;font-weight:bold;border-bottom:1px solid #404a4b;display:flex;align-items:center;cursor:pointer}.pageList .page-head>.name{padding:8px;padding-left:15px;flex-grow:1;flex-shrink:1;overflow:hidden;text-overflow:ellipsis}.pageList .page-head>.name:hover{background-color:#4f85f2}.pageList .page-head>.hint{flex-shrink:0;font-size:0.7em}.pageList .page-head.active{background-color:#092d76}.pageList .layerListWrapper{display:flex;flex-direction:column;width:100%;height:100%}.pageList .layerListWrapper .back{flex-shrink:0;background:#4c5759;display:flex;align-items:center}.pageList .layerListWrapper .back .backBtn{padding:15px;cursor:pointer;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none}.pageList .layerListWrapper .back .backBtn:hover{background:#576567}.pageList .layerListWrapper .layerList{flex:1 1 auto}.pageList .pageLayerInfo{padding:8px;font-size:0.8em;text-align:center}.pageList .itemBtn{align-self:stretch;width:40px;flex-shrink:0;flex-grow:0;display:flex;align-items:center;justify-content:center;background-color:#2f3738;border-left:1px solid #343c3d;border-right:1px solid #1d2122}.pageList .itemBtn:hover{color:#2ecc71}\n", ""])
		},
		487: function(e, t, o) {
			var r = o(488);
			"string" == typeof r && (r = [
				[e.i, r, ""]
			]);
			var s = {
				hmr: !0,
				transform: void 0,
				insertInto: void 0
			};
			o(21)(r, s);
			r.locals && (e.exports = r.locals)
		},
		488: function(e, t, o) {
			(e.exports = o(20)(!1))
			.push([e.i, ".layerList{overflow-y:auto}\n", ""])
		},
		54: function(e, t, o) {
			"use strict";
			o.r(t);
			var r = o(96),
				s = o(0),
				a = o(6);
			async function i(e) {
				const t = s.b.getState()
					.sessionState.adapters,
					o = {
						meta: {
							name: e.name,
							mime: e.type
						},
						file: e
					};
				for (const e of t)
					if (e.checkFileMeta(o.meta)) try {
						Object(a.a)("filetype_" + e.fileTypeName);
						const t = await e.decodeProject(o);
						return await t.getPages(), s.b.dispatch({
							type: "openProject",
							project: t
						}), t
					} catch (e) {
						return Object(a.a)("fileparse_err_" + e.toString()), Promise.reject(e)
					}
				return Object(a.a)("no_mime_" + o.meta.name), Promise.reject(Object(r.lang)("error_openfile_no_adapter", o.meta.name))
			}
			var n = o(1);
			const c = [.1, .25, .33, .5, .75, 1, 1.5, 2, 3, 4],
				l = [1, 2, 3, 4];

			function d() {
				const e = f();
				var t;
				h((t = e, c.find(e => e > t) || c[c.length - 1]))
			}

			function p(e) {
				const t = s.b.getState()
					.canvasState.renderer;
				if (t) {
					const o = {
							x: t.renderWidth / 2,
							y: t.renderHeight / 2
						},
						r = {
							x: e.x * f() - o.x,
							y: e.y * f() - o.y
						};
					t.panX(r.x), t.panY(r.y)
				}
			}

			function u(e) {
				Object(a.a)("center_to_page");
				const t = e.offsetX ? e.offsetX : 0,
					o = e.offsetY ? e.offsetY : 0;
				p(new n.Rect(t, o, e.width + t, e.height + o)
					.centerPoint)
			}

			function h(e) {
				const t = s.b.getState()
					.canvasState.renderer;
				if (t) {
					const o = t.rendererPointToRealPoint({
						x: t.renderWidth / 2,
						y: t.renderHeight / 2
					});
					s.b.dispatch({
						value: e,
						type: "canvasStateZoomLevel"
					}), p(o)
				}
			}

			function b(e) {
				s.b.dispatch({
					type: "canvasStateMetricScale",
					value: e
				})
			}

			function m() {
				h(y(f()))
			}

			function f() {
				return s.b.getState()
					.canvasState.zoomLevel
			}

			function g() {
				const e = s.b.getState()
					.canvasState.renderer,
					t = Math.round(1 / Math.max(e.imgWidth / e.renderWidth, e.imgHeight / e.renderHeight, 1) * 100) / 100,
					o = -1 !== c.indexOf(t) ? t : y(t);
				p({
					x: e.imgWidth / 2,
					y: e.imgHeight / 2
				}), h(o)
			}

			function y(e) {
				return c.concat()
					.reverse()
					.find(t => t < e) || c[0]
			}
			let v;
			async function x(e) {
				const {
					SvgRenderer: t
				} = await Promise.all([o.e(1), o.e(29)])
					.then(o.bind(null, 900));
				v && (v.destroy(), v.off("mousemove", O)), v = new t(e),
					function(e) {
						e.on("mousemove", O)
					}(v), s.b.dispatch({
						type: "canvasStateRenderer",
						renderer: v
					});
				const r = await s.b.getState()
					.sessionState.project.getPages();
				return await v.renderPages(r), g(), v
			}
			let w = null;

			function O(e) {
				if (e) {
					const t = v.realPointApplyMetricScale(v.rendererPointToRealPoint(v.mouseEventToCoords(e)));
					s.b.dispatch({
						type: "canvasStateMouseCoords",
						coords: t
					})
				}
			}
			window.addEventListener("resize", () => {
				w && (clearTimeout(w), w = null), v && (w = window.setTimeout(() => {
					w = null, v && v.resizeRender()
				}, 100))
			});
			var j = o(459),
				C = o.n(j);
			const L = [new class {
				constructor() {
					this.name = Object(r.lang)("download"), this.iconCls = "fas fa-download"
				}
				exportBlob(e, t) {
					return C.a.saveAs(e, t), Promise.resolve()
				}
			}];
			var S = o(31);
			const k = o(460)
				.Toast;
			const P = new class {
				info(e) {
					k(e, {
						style: {
							main: {
								bottom: "auto",
								top: "10%",
								left: "auto",
								width: "auto",
								right: "8%",
								background: "rgba(255,255,255,0.85",
								color: "black"
							}
						},
						settings: {
							duration: 800
						}
					})
				}
			};

			function T(e) {
				const t = S.color.rgbStrToHex(e)
					.toUpperCase();
				Object(S.copyToClipboard)(t), P.info("Copied " + t)
			}
			o.d(t, "projectOpenLocalFile", (function() {
				return i
			})), o.d(t, "initRendererOnCanvas", (function() {
				return x
			})), o.d(t, "metricScales", (function() {
				return l
			})), o.d(t, "zoomIn", (function() {
				return d
			})), o.d(t, "centerTo", (function() {
				return p
			})), o.d(t, "centerToPage", (function() {
				return u
			})), o.d(t, "setZoom", (function() {
				return h
			})), o.d(t, "setMetricScale", (function() {
				return b
			})), o.d(t, "zoomOut", (function() {
				return m
			})), o.d(t, "getZoom", (function() {
				return f
			})), o.d(t, "fitToPage", (function() {
				return g
			})), o.d(t, "exporters", (function() {
				return L
			})), o.d(t, "copyColor", (function() {
				return T
			}))
		},
		74: function(e, t, o) {
			"use strict";
			o.r(t), o.d(t, "lang", (function() {
				return c
			}));
			var r = o(54);
			o.d(t, "facade", (function() {
				return r
			}));
			var s = o(105);
			o.d(t, "tools", (function() {
				return s
			}));
			var a = o(96);
			o.d(t, "i18n", (function() {
				return a
			}));
			var i = o(1);
			o.d(t, "Core", (function() {
				return i
			}));
			var n = o(31);
			o.d(t, "util", (function() {
				return n
			}));
			const c = a.lang
		},
		897: function(e, t, o) {
			"use strict";
			o.r(t);
			var r = o(4),
				s = (o(455), o(457), o(74)),
				a = o(33),
				i = o(54);
			class n extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						coords: e => e.canvasState.mouseCoords,
						zoomLevel: e => e.canvasState.zoomLevel,
						curTool: e => e.toolState.curTool,
						renderer: e => e.canvasState.renderer,
						metricScale: e => e.canvasState.metricScale
					})
				}
				componentDidMount() {
					s.facade.initRendererOnCanvas(this.canvasWrapper)
				}
				componentWillUnmount() {}
				zoomIn() {
					s.facade.zoomIn()
				}
				zoomOut() {
					s.facade.zoomOut()
				}
				otherScale() {
					const e = window.prompt("Please enter a scale number", "" + this.state.metricScale);
					if (e) {
						const t = parseFloat(e);
						!isNaN(t) && t > 0 && s.facade.setMetricScale(t)
					}
				}
				render() {
					return Object(r.b)("div", {
						ref: e => this.canvasWrapper = e,
						class: `canvas ${this.state.curTool?this.state.curTool.slug:""}`
					}, this.state.renderer ? Object(r.b)("div", {
						class: "tools"
					}, Object(r.b)("div", null, this.state.coords ? Object(r.b)("span", {
						class: "text"
					}, "X: ", this.state.coords.x) : null, this.state.coords ? Object(r.b)("span", {
						class: "text"
					}, "Y: ", this.state.coords.y) : null), Object(r.b)("div", {
						class: "metricScale dropdown is-hoverable is-up"
					}, Object(r.b)("div", {
						class: "dropdown-trigger"
					}, Object(r.b)("i", {
						class: "fas fa-ruler-combined"
					}), Object(r.b)("span", {
						class: "metrics"
					}, "@", this.state.metricScale, "X")), Object(r.b)("div", {
						class: "dropdown-menu",
						id: "dropdown-menu7",
						role: "menu"
					}, Object(r.b)("div", {
						class: "dropdown-content"
					}, Object(r.b)("div", {
						class: "dropdown-item "
					}, Object(r.b)("p", null, "选择已创建的公制比例。它会影响画布测量.")), Object(r.b)("hr", {
						class: "dropdown-divider"
					}), i.metricScales.map(e => Object(r.b)("div", {
						onClick: () => {
							s.facade.setMetricScale(e)
						},
						class: "dropdown-item selectable"
					}, Object(r.b)("p", null, "@", e, "X"))), Object(r.b)("hr", {
						class: "dropdown-divider"
					}), Object(r.b)("div", {
						onClick: () => {
							this.otherScale()
						},
						class: "dropdown-item selectable"
					}, Object(r.b)("p", null, "Other"))))), Object(r.b)("div", {
						class: "zoomTool"
					}, Object(r.b)("span", {
						class: "zoom",
						onClick: () => this.zoomOut()
					}, Object(r.b)("i", {
						class: "fas fa-search-minus"
					})), Object(r.b)("span", {
						class: "text"
					}, 100 * (this.state.zoomLevel || 1), "%"), Object(r.b)("span", {
						class: "zoom",
						onClick: () => this.zoomIn()
					}, Object(r.b)("i", {
						class: "fas fa-search-plus"
					})))) : null)
				}
			}
			o(461), o(463), o(465), o(467);
			class c extends r.a {
				constructor(e) {
					super(e), this.state = {
						value: "string" == typeof e.value ? e.value : "",
						copied: !1
					}, e.value instanceof Promise && e.value.then(e => {
						this.setState({
							value: e
						})
					})
				}
				get label() {
					if (this.props.children) {
						if (this.props.children instanceof Array && this.props.children.length > 0) return this.props.children;
						if (!(this.props.children instanceof Array)) return this.props.children
					}
					return this.state.value
				}
				componentWillReceiveProps(e) {
					e.value instanceof Promise ? e.value.then(e => {
						this.setState({
							value: e
						})
					}) : this.setState({
						value: e.value
					})
				}
				copy() {
					s.util.copyToClipboard(this.state.value), this.setState({
						copied: !0
					}), setTimeout(() => {
						this.setState({
							copied: !1
						})
					}, 1e3)
				}
				render() {
					return Object(r.b)("div", {
						onClick: () => this.copy(),
						class: "clickCopyDiv"
					}, Object(r.b)("span", {
						class: "label"
					}, this.label), this.state.copied ? Object(r.b)("div", {
						class: "copied animated fadeOutUp"
					}, s.i18n.lang("copied") + "!") : null)
				}
			}
			var l = o(36);
			class d extends class {
				toCode(e) {
					const t = e.style;
					return t ? Object.keys(t)
						.map(o => {
							const r = t[o];
							return this.styleToCode(o, r, e)
						})
						.filter(e => e.length > 0)
						.join("\n") : ""
				}
			} {
				styleToCode(e, t, o) {
					return ["left", "top", "right", "bottom"].indexOf(e) > -1 ? `${e}: ${t}px;` : "opacity" === e ? `opacity:${t}` : Object(l.e)(o) ? "fontFamily" === e ? `font-family:${t}` : "fontSize" === e ? `font-size:${t}px` : "fontWeight" === e ? `font-weight:${t}` : "fontStyle" === e ? `font-style:${t}` : "textDecoration" === e ? `text-decoration:${t}` : "textAlign" === e ? `align:${t}` : "fill" === e ? `color:${t}` : "" : "fill" === e ? `background-color:${t}` : ""
				}
			}
			d.codeType = "CSS";
			class p extends r.a {
				constructor() {
					super(), this.state = {
						showCss: !1
					}
				}
				renderTable() {
					const e = this.props.layer;
					return Object(r.b)("table", null, Object(r.b)("tr", null, Object(r.b)("td", null, "Name"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.name
					}))), Object(r.b)("tr", null, Object(r.b)("td", null, "Type"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.layerType
					}))), Object(r.b)("tr", null, Object(r.b)("td", null, "Left"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.rect.left.toString()
					}))), Object(r.b)("tr", null, Object(r.b)("td", null, "Top"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.rect.top.toString()
					}))), Object(r.b)("tr", null, Object(r.b)("td", null, "Width"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.rect.width.toString()
					}))), Object(r.b)("tr", null, Object(r.b)("td", null, "Height"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.rect.height.toString()
					}))), this.renderTextProps(), e.style ? this.renderObject(e.style) : null)
				}
				renderStyles() {}
				renderObject(e, t = "") {
					let o = [];
					Object.keys(e)
						.filter(t => void 0 !== e[t])
						.forEach(s => {
							const a = e[s],
								i = t + s;
							"object" == typeof a ? o = o.concat(this.renderObject(a, i + "-")) : o.push(Object(r.b)("tr", null, Object(r.b)("td", null, i), Object(r.b)("td", null, Object(r.b)(c, {
								value: a.toString()
							}))))
						});
					return o
				}
				renderTextProps() {
					const e = this.props.layer;
					return Object(l.e)(e) ? Object(r.b)("tr", null, Object(r.b)("td", null, "Text"), Object(r.b)("td", null, Object(r.b)(c, {
						value: e.getText()
					}))) : null
				}
				renderCss() {
					const e = this.props.layer,
						t = (new d)
						.toCode(e);
					return Object(r.b)("pre", null, t)
				}
				render() {
					return Object(r.b)("div", {
						class: "card layerBasicView"
					}, Object(r.b)("div", {
						class: "title has-background-grey-dark has-text-white-ter"
					}, "图层信息", Object(r.b)("span", {
						class: "flex1"
					}), Object(r.b)("span", {
						onClick: () => {
							this.setState({
								showCss: !this.state.showCss
							})
						},
						class: `showCss ${this.state.showCss?"has-text-info":""}`
					}, "CSS"), " "), Object(r.b)("div", {
						class: "cardBody"
					}, this.state.showCss ? this.renderCss() : this.renderTable()))
				}
			}
			o(469), o(471);
			class u extends r.a {
				render() {
					return Object(r.b)("div", {
						onClick: this.props.onClick,
						class: `squareButton ${this.props.active?"active":""}`
					}, Object(r.b)("div", {
						class: "head"
					}, this.props.head), Object(r.b)("span", {
						class: "value"
					}, this.props.value))
				}
			}
			var h = o(31);
			async function b(e, t) {
				const {
					layerToSvg: r
				} = await Promise.all([o.e(1), o.e(7)])
					.then(o.bind(null, 896));
				if (Object(l.d)(e)) return Object(h.scaleCanvas)(await e.getPixelImg(), t); {
					const o = await r(e);
					return Object(h.svgToCanvas)(o.svg(), t)
				}
			}
			var m = o(6);

			function f(e) {
				const t = h.CanvasExportFormats.concat([]);
				return t.push("image/svg"), t
			}

			function g(e, t) {
				return t && "image/svg" !== t.format ? `${e.name}@${t.scale}X.${t.format.replace("image/","")}` : `${e.name}.svg`
			}
			async function y(e, t, r) {
				const {
					optimizeJpg: s,
					optimizePng: a
				} = await o.e(8)
					.then(o.bind(null, 895));
				if (Object(m.a)("export_trim_" + r.trim), Object(m.a)("export_scale_" + r.scale), Object(m.a)("export_optimize_" + r.optimize), Object(m.a)("export_format_" + r.format), "image/svg" === r.format) return async function(e, t, r) {
					const {
						optimizeSvg: s
					} = await o.e(8)
						.then(o.bind(null, 895)), {
							layerToSvg: a
						} = await Promise.all([o.e(1), o.e(7)])
						.then(o.bind(null, 896));
					let i = (await a(e))
						.svg();
					r && (i = await s(i));
					return t.exportBlob(new Blob([i], {
						type: "image/svg+xml"
					}), g(e))
				}(e, t, r.optimize); {
					let o = await b(e, r.scale);
					r.trim && (o = Object(h.trimCanvas)(o));
					let i = null;
					const n = g(e, r);
					if (r.optimize && "image/png" === r.format) {
						const e = o.getContext("2d");
						i = await a(e.getImageData(0, 0, o.width, o.height)
							.data, o.width, o.height)
					}
					return i || (i = await Object(h.canvasToFile)(o, n, r.format, 100), r.optimize && "image/jpeg" === r.format && (i = await s(i))), t.exportBlob(i, n)
				}
			}
			var v = o(162);
			class x extends r.a {
				constructor(e) {
					super(e), this.previewUrl = "", this.imgEle = void 0, this.state = {
						exportImgParams: {
							trim: !0,
							scale: 1,
							format: f(this.props.layer)[0],
							optimize: !0
						}
					}
				}
				get imgParamsFormat() {
					return this.state.exportImgParams.format
				}
				set imgParamsFormat(e) {
					this.state.exportImgParams.format !== e && this.setState({
						exportImgParams: {
							...this.state.exportImgParams,
							format: e
						}
					})
				}
				get exportFileName() {
					return g(this.props.layer, this.state.exportImgParams)
				}
				componentDidMount() {
					this.updatePreview()
				}
				async updatePreview() {
					const e = await async function(e, t) {
						let o = await b(e, t.scale);
						return t.trim && (o = Object(h.trimCanvas)(o)), o.toDataURL(t.format, 100)
					}(this.props.layer, this.state.exportImgParams);
					e !== this.previewUrl && (this.previewUrl = e, this.forceUpdate())
				}
				componentDidUpdate(e, t) {
					this.state.exportImgParams === t.exportImgParams && this.props.layer === e.layer || this.updatePreview()
				}
				componentWillReceiveProps(e) {
					this.props.layer, e.layer
				}
				render() {
					return Object(r.b)("div", {
						class: "card layerExportView"
					}, Object(r.b)("div", {
						class: "title has-background-grey-dark has-text-white-ter"
					}, "显示"), Object(r.b)("div", {
						class: "cardBody"
					}, this.renderImageParam(), this.renderFileExporters()))
				}
				async exportFile(e) {
					await y(this.props.layer, e, this.state.exportImgParams)
				}
				renderFileExporters() {
					return Object(r.b)("div", {
						class: "fileExport"
					}, Object(r.b)("div", {
						class: "fileName"
					}, "Name: ", this.exportFileName), Object(r.b)("div", {
						class: "exporters"
					}, i.exporters.map(e => Object(r.b)(v.a, {
						onClick: () => this.exportFile(e),
						class: "button is-primary is-small"
					}, Object(r.b)("i", {
						class: e.iconCls
					}), e.name))))
				}
				toogleTrim() {
					this.setState({
						exportImgParams: {
							...this.state.exportImgParams,
							trim: !this.state.exportImgParams.trim
						}
					})
				}
				setImgScale(e) {
					e !== this.state.exportImgParams.scale && this.setState({
						exportImgParams: {
							...this.state.exportImgParams,
							scale: e
						}
					})
				}
				toggleOptimize() {
					this.setState({
						exportImgParams: {
							...this.state.exportImgParams,
							optimize: !this.state.exportImgParams.optimize
						}
					})
				}
				renderImageParam() {
					return Object(r.b)("div", {
						class: "imgParam"
					}, Object(r.b)("div", {
						class: "preview",
						style: {
							backgroundImage: "url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAABfGlDQ1BJQ0MgUHJvZmlsZQAAKJFjYGAqSSwoyGFhYGDIzSspCnJ3UoiIjFJgv8PAzcDDIMRgxSCemFxc4BgQ4MOAE3y7xsAIoi/rgsxK8/x506a1fP4WNq+ZclYlOrj1gQF3SmpxMgMDIweQnZxSnJwLZOcA2TrJBUUlQPYMIFu3vKQAxD4BZIsUAR0IZN8BsdMh7A8gdhKYzcQCVhMS5AxkSwDZAkkQtgaInQ5hW4DYyRmJKUC2B8guiBvAgNPDRcHcwFLXkYC7SQa5OaUwO0ChxZOaFxoMcgcQyzB4MLgwKDCYMxgwWDLoMjiWpFaUgBQ65xdUFmWmZ5QoOAJDNlXBOT+3oLQktUhHwTMvWU9HwcjA0ACkDhRnEKM/B4FNZxQ7jxDLX8jAYKnMwMDcgxBLmsbAsH0PA4PEKYSYyjwGBn5rBoZt5woSixLhDmf8xkKIX5xmbARh8zgxMLDe+///sxoDA/skBoa/E////73o//+/i4H2A+PsQA4AJHdp4IxrEg8AAACDSURBVDgR3VJBEgARDGtx4gH+/y4P8AGu7Masm2zvejGTaDQN7b3Pt4RVrVXGGEfaOSchxngkAaKxtfYvQLs/AtOxCYE7S8DiLxAIbEHwjhQQFStwWkqZLGdVlZyz4DzV+gdWzikl8d6f+he2LDAbwNnrW5Eb3DeM8wKBYOZMItyreQC1XjqT76HMaAAAAABJRU5ErkJggg==)"
						}
					}, Object(r.b)("div", {
						class: "mask"
					}), this.previewUrl ? Object(r.b)("img", {
						ref: e => this.imgEle = e,
						src: this.previewUrl
					}) : null, this.previewUrl ? Object(r.b)("div", {
						class: "size"
					}, this.props.layer.rect.width * this.state.exportImgParams.scale, "x", this.props.layer.rect.height * this.state.exportImgParams.scale) : null), Object(r.b)("div", {
						class: "options"
					}, Object(r.b)(u, {
						onClick: () => {
							this.toogleTrim()
						},
						head: "修剪",
						value: this.state.exportImgParams.trim ? "打开" : "关闭",
						active: this.state.exportImgParams.trim
					}), Object(r.b)(u, {
						onClick: () => {
							this.toggleOptimize()
						},
						head: "优化",
						value: `${this.state.exportImgParams.optimize?"打开":"关闭"}`,
						active: this.state.exportImgParams.optimize
					}), Object(r.b)("div", {
						class: "dropdown is-hoverable"
					}, Object(r.b)(u, {
						head: "大小",
						value: `${this.state.exportImgParams.scale}X`
					}), Object(r.b)("div", {
						class: "dropdown-menu"
					}, Object(r.b)("div", {
						class: "dropdown-content"
					}, [.25, .5, .75, 1, 2, 3, 4].map(e => Object(r.b)("a", {
						href: "javascript:;",
						onClick: () => {
							this.setImgScale(e)
						},
						class: "dropdown-item"
					}, e, "X")), Object(r.b)("hr", {
						class: "dropdown-divider"
					}), Object(r.b)("a", {
						onClick: () => {
							const e = parseFloat(window.prompt("Please enter scale number: ", this.state.exportImgParams.scale.toString()));
							this.setImgScale(e)
						},
						href: "javascript:;",
						class: "dropdown-item"
					}, "Other Scale")))), Object(r.b)("div", {
						class: "dropdown is-hoverable"
					}, Object(r.b)(u, {
						head: "格式",
						value: `${this.imgParamsFormat.replace("image/","").toUpperCase()}`
					}), Object(r.b)("div", {
						class: "dropdown-menu"
					}, Object(r.b)("div", {
							class: "dropdown-content"
						}, f(this.props.layer)
						.map(e => Object(r.b)("a", {
								href: "javascript:;",
								onClick: () => {
									this.imgParamsFormat = e
								},
								class: "dropdown-item"
							}, e.replace("image/", "")
							.toUpperCase())))))))
				}
			}
			var w = o(131),
				O = (o(473), o(0)),
				j = o(202);
			class C extends r.a {
				constructor(e) {
					super(e), this.state = {
						active: !1
					};
					let t = void 0;
					Object(a.a)(this, {
						active: async o => {
							if (e.showActive) {
								if (t === o.toolState.choseLayers) return this.state.active;
								const r = e.layer;
								if (t = o.toolState.choseLayers, Object(l.b)(r)) {
									for (const e of t) {
										if (await Object(j.b)(r, e)) return !0
									}
									return !1
								}
								return t.indexOf(r) > -1
							}
							return !1
						}
					})
				}
				componentWillReceiveProps(e) {
					e.layer !== this.props.layer && this.setState({
						children: void 0
					})
				}
				getIcon(e) {
					switch (e.layerType) {
						case s.Core.LayerType.text:
							return "fa-font";
						case s.Core.LayerType.folder:
						case s.Core.LayerType.folderVector:
							return "fa-folder";
						case s.Core.LayerType.pixel:
							return "fa-file-image";
						case s.Core.LayerType.vector:
							return "fa-vector-square"
					}
				}
				layerItemClick(e, t) {
					t.metaKey || t.shiftKey || t.ctrlKey ? (Object(m.a)("choose_layer_list_multi"), O.b.dispatch({
						type: "toolStateToggleChoseLayer",
						layers: [e]
					})) : (Object(m.a)("choose_layer_list_single"), O.b.dispatch({
						type: "toolStateChoseLayer",
						layers: [e]
					}))
				}
				async toggleFolder(e) {
					if (Object(m.a)("ui_toggle_folder"), Object(l.b)(e))
						if (this.state.children) this.setState({
							children: void 0
						});
						else {
							const t = await e.children();
							this.setState({
								children: t
							})
						}
				}
				renderChildren() {
					const e = this.props.indent ? this.props.indent : 0;
					return this.state.children.map(t => Object(r.b)(C, {
						showActive: this.props.showActive,
						layer: t,
						indent: e + 1
					}))
				}
				renderLayerItem() {
					const e = this.props.layer,
						t = this.props.indent ? this.props.indent : 0,
						o = this.state.active;
					return Object(r.b)("div", null, Object(r.b)("div", {
						class: "layerItemContainer"
					}, Object(r.b)("div", {
						class: "layerItem " + (e.visible ? "" : "invisible") + (o ? " active" : ""),
						onClick: t => {
							this.layerItemClick(e, t)
						}
					}, Object(r.b)("span", {
						class: "indent",
						style: {
							width: 16 * t
						}
					}), Object(r.b)("span", {
						class: "icon is-small"
					}, Object(r.b)("i", {
						class: "fas " + this.getIcon(e),
						"aria-hidden": "true"
					})), Object(r.b)("span", {
						class: "name"
					}, e.name)), Object(l.b)(e) ? Object(r.b)("div", {
						onClick: () => {
							this.toggleFolder(e)
						},
						class: "folderToggle itemBtn"
					}, this.state.children ? Object(r.b)("i", {
						class: "far fa-minus-square"
					}) : Object(r.b)("i", {
						class: "far fa-plus-square"
					})) : null), this.state.children ? this.renderChildren() : null)
				}
				render() {
					return this.renderLayerItem()
				}
			}
			o(475);
			class L extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						nearbyLayers: e => e.toolState.neabyLayers
					})
				}
				render() {
					if (!this.state.nearbyLayers || 0 === this.state.nearbyLayers.length) return null;
					const e = this.state.nearbyLayers.slice(1);
					return Object(r.b)("div", {
						class: "nearByLayers"
					}, Object(r.b)("div", {
						class: "pageList"
					}, Object(r.b)("h3", {
						class: "page-head"
					}, "Near by Layers"), Object(r.b)("div", {
						class: "layerList"
					}, e.map(e => Object(r.b)(C, {
						showActive: !1,
						layer: e
					})))))
				}
			}
			class S extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						layers: e => e.toolState.choseLayers,
						display: e => e.toolState.choseLayers.length > 0
					})
				}
				get curLayer() {
					return Object(w.b)(this.state.layers)
				}
				render() {
					return this.state.display && this.curLayer ? Object(r.b)("div", null, Object(r.b)(L, null), Object(r.b)(x, {
						layer: this.curLayer
					}), Object(r.b)(p, {
						layer: this.curLayer
					})) : null
				}
			}
			var k = o(105);
			o(477);
			class P extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						display: e => !!e.toolState.curTool && e.toolState.curTool instanceof k.ColorTool,
						colors: e => e.toolState.pickedColors,
						hoverColor: e => e.toolState.hoverColor
					})
				}
				copyColor(e) {
					s.facade.copyColor(e)
				}
				render() {
					return this.state.display ? Object(r.b)("div", {
						class: "card colorPickedView"
					}, Object(r.b)("div", {
						class: "title"
					}, "Colors"), Object(r.b)("div", {
						class: "cardBody"
					}, Object(r.b)("div", {
						class: "hoverColor"
					}, Object(r.b)("div", {
						class: "preview",
						style: `background-color:${this.state.hoverColor}`
					}), Object(r.b)("div", {
						class: "txt"
					}, Object(r.b)("span", {
							class: "hex"
						}, s.util.color.rgbStrToHex(this.state.hoverColor)
						.toUpperCase()), Object(r.b)("span", null, "Click to Copy"))), Object(r.b)("div", {
						class: "pickedColors"
					}, this.state.colors.map(e => Object(r.b)("div", {
						onClick: () => this.copyColor(e),
						class: "pickedColor"
					}, Object(r.b)("div", {
						class: "preview",
						style: `background-color:${e}`
					}), Object(r.b)("span", null, s.util.color.rgbStrToHex(e)
						.toUpperCase())))))) : null
				}
			}
			class T extends r.a {
				get curToolState() {
					return O.b.getState()
						.toolState.curTool
				}
				componentDidMount() {}
				componentWillUnmount() {}
				render() {
					return Object(r.b)("aside", {
						class: "sidebar sidebarRight"
					}, Object(r.b)(P, null), Object(r.b)(S, null))
				}
			}
			o(479), o(481);
			class I extends r.a {
				onToolClick(e) {
					return Object(m.a)(`tool_${this.props.tool.name}`), this.props.onClick(e)
				}
				render() {
					return Object(r.b)("div", {
						onClick: e => this.onToolClick(e),
						class: `toolBtn toggle ${this.props.tool.activated?"active":""}`
					}, this.props.tool.isClsSVG ? Object(r.b)("img", {
						src: this.props.tool.cls
					}) : Object(r.b)("i", {
						class: this.props.tool.cls
					}))
				}
			}
			class M extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						display: e => !!e.canvasState.renderer,
						showListColumn: e => e.viewState.showListColumn,
						curTool: e => e.toolState.curTool
					}), this.unsubscribe = Object(O.a)(e => e.toolState.choseLayers, () => {
						this.setExclusiveTool(Object(k.getToolInst)(k.InspectTool))
					}, !1)
				}
				componentWillUnmount() {
					this.unsubscribe()
				}
				render() {
					return this.state.display ? Object(r.b)("div", {
						class: "toolbar1"
					}, this.renderPillarTool(), Object(r.b)("div", {
						class: "flex1"
					}), this.renderGeneralTools()) : null
				}
				renderGeneralTools() {
					return s.tools.tools.general.map(e => this.bindToggleTool(e))
						.concat([Object(r.b)("div", {
							onClick: () => O.b.dispatch({
								type: "viewStateToggleListItem"
							}),
							class: `toolBtn toggle toolLayers ${this.state.showListColumn?"active":""}`
						}, Object(r.b)("i", {
							class: "fas fa-th"
						}))])
				}
				renderPillarTool() {
					return s.tools.tools.inspect.map(e => this.bindExclusiveTool(e))
				}
				async setExclusiveTool(e) {
					await s.tools.setActiveTool(e), this.forceUpdate()
				}
				bindExclusiveTool(e) {
					return e ? Object(r.b)(I, {
						onClick: () => this.setExclusiveTool(e),
						tool: e
					}) : null
				}
				async toggleTool(e) {
					await s.tools.toggleTool(e), this.forceUpdate()
				}
				bindToggleTool(e) {
					if (e) return Object(r.b)(I, {
						onClick: () => this.toggleTool(e),
						tool: e
					})
				}
			}
			o(483), o(485), o(487);
			class z extends r.a {
				constructor() {
					super()
				}
				get pageState() {
					return this.props.page
				}
				componentWillUnmount() {
					this.unsubscribe()
				}
				componentDidMount() {
					this.renderLayerItem(), this.unsubscribe = O.b.subscribe(() => {
						this.pageState && this.pageState !== this.currentPage && this.renderLayerItem()
					})
				}
				async renderLayerItem() {
					this.currentPage = this.pageState;
					const e = await this.currentPage.getLayers();
					this.setState({
						layers: e
					})
				}
				render() {
					const {
						layers: e
					} = this.state;
					return Object(r.b)("div", {
						class: "layerList"
					}, e && e.map((e, t, o) => Object(r.b)(C, {
						showActive: !0,
						layer: e
					})))
				}
			}
			class A extends r.a {
				constructor() {
					super(), Object(a.b)(this, {
						choseLayers: e => e.toolState.choseLayers
					}), this.state = {
						collapsedPages: []
					}
				}
				async componentDidMount() {
					const e = O.b.getState()
						.sessionState.project;
					if (e) {
						const t = await e.getPages();
						this.setState({
							pages: t
						})
					}
				}
				async chosePage(e) {
					Object(m.a)("choose_page"), O.b.dispatch({
						type: "toolStateChoseLayer",
						layers: [await e.toLayer()]
					})
				}
				togglePage(e) {
					const t = this.state.collapsedPages.concat([]),
						o = t.indexOf(e);
					o > -1 ? t.splice(o, 1) : t.push(e), this.setState({
						collapsedPages: t
					})
				}
				renderPage(e) {
					const t = !(!this.state.choseLayers || !this.state.choseLayers.find(t => t.page === e));
					return Object(r.b)("div", {
						class: "pageWrapper"
					}, Object(r.b)("div", {
						class: `page-head ${t?"active":""}`
					}, Object(r.b)("div", {
						onClick: () => this.chosePage(e),
						class: "name"
					}, e.name), Object(r.b)("div", {
						onClick: () => Object(i.centerToPage)(e),
						class: "itemBtn"
					}, Object(r.b)("i", {
						class: "fas fa-crosshairs"
					})), Object(r.b)("div", {
						class: "itemBtn",
						onClick: () => this.togglePage(e)
					}, -1 === this.state.collapsedPages.indexOf(e) ? Object(r.b)("i", {
						class: "far fa-minus-square"
					}) : Object(r.b)("i", {
						class: "far fa-plus-square"
					}))), -1 === this.state.collapsedPages.indexOf(e) ? Object(r.b)("div", {
						class: "layerListWrapper"
					}, Object(r.b)(z, {
						page: e
					})) : null, Object(r.b)("div", {
						class: "pageLayerInfo"
					}, "(", e.layerAmount, " Layers)"))
				}
				render() {
					return Object(r.b)("div", {
						class: "pageList"
					}, this.state.pages ? this.state.pages.map(e => this.renderPage(e)) : null)
				}
			}
			class B extends r.a {
				constructor() {
					super(), Object(a.a)(this, {
						display: e => !!e.sessionState.project,
						showListColumn: e => e.viewState.showListColumn
					})
				}
				render() {
					return this.state.display ? Object(r.b)("aside", {
						class: "sidebar sidebarLeft"
					}, Object(r.b)(M, null), this.state.showListColumn ? Object(r.b)(A, null) : null) : null
				}
			}
			var D = o(159),
				E = o(323),
				R = o(161);
			class _ extends r.a {
				constructor(e) {
					super(e), this.curChoseLayers = [], this.selectCount = 0, this.NEXT_AD_PER_COUNT = 5, this.state = {
						showAd: !1,
						selectCount: 0,
						showClose: !1
					}
				}
				get shouldNextAd() {
					const e = O.b.getState();
					return e.toolState.choseLayers != this.curChoseLayers && (this.curChoseLayers = e.toolState.choseLayers, this.selectCount += 1, this.selectCount > 0 && this.selectCount % this.NEXT_AD_PER_COUNT == 0)
				}
				componentDidMount() {
					O.b.subscribe(() => {
						this.shouldNextAd && this.nextAd()
					})
				}
				componentDidUpdate() {
					this.state.showAd && !1 === this.state.showClose && window && (window.adsbygoogle = window.adsbygoogle || [])
						.push({})
				}
				nextAd(e = !1) {
					Object(m.a)("ad_show"), this.setState({
						showAd: !1,
						showClose: e
					}), setTimeout(() => {
						this.state.showAd || (this.setState({
							showAd: !0
						}), setTimeout(() => {
							this.state.showClose || this.setState({
								showClose: !0
							})
						}, Math.round(3500 * (Math.random() + 1))))
					}, 100)
				}
			
				renderHint() {
					return Object(r.b)("div", null, "Ads will show in 5 selections")
				}
				render() {
					return Object(r.b)("div")
				}
			}
			_.defaultProps = {
				class: "",
				style: {
					display: "block"
				},
				format: "auto",
				layout: "",
				layoutKey: "",
				responsive: "true"
			};
			var U = o(9);
			o.d(t, "InspectStudio", (function() {
				return V
			}));
			class V extends r.a {
				constructor() {
					super(), this.onToggleView = e => {}, Object(a.b)(this, {
						project: e => e.sessionState.project
					}), this.state = {
						loading: !1,
						showMainAd: !0
					}
				}
				renderMain() {
					return window.document.title = this.state.project.name, Object(r.b)("div", {
						class: "mainContainer flexColumn"
					}, Object(r.b)(B, null), Object(r.b)("main", {
						class: "flexItem main"
					}, Object(U.c)() ? Object(r.b)(_, {
						client: "ca-pub-3813796308245340",
						slot: "3166566114",
						format: "auto",
						responsive: "false"
					}) : null, Object(r.b)("div", {
						class: "canvasWrapper"
					}, Object(r.b)(n, null))), Object(r.b)(T, null))
				}
				async onLoadFile(e) {
					//console.log(e);
					//console.log(nerirong1);
					this.setState({
						loading: !0
					});
					const {
						facade: t
					} = await Promise.resolve()
						.then(o.bind(null, 74));
					t.projectOpenLocalFile(nerirong1)
						.then(async () => {}, e => {
							Object(E.b)(e)
						})
						.then(() => {
							this.setState({
								loading: !1
							})
						})
				}
				renderLoader() {
					return Object(r.b)("div", {
						class: "fileLoaderWrapper"
					}, Object(r.b)("div", {
						class: "formatsWrapper"
					}, Object(r.b)(R.a, null)), Object(r.b)(D.a, {
						onFile: e => {
							this.onLoadFile(e)
						}
					}))
				}
				renderLoading() {
					return Object(r.b)("div", {
						class: "loading is-size-4"
					}, Object(r.b)("i", {
						class: "fas fa-spinner is-size-2  animated infinite spin"
					}), " 解析... 请耐心点.")
				}
				render() {
					return Object(r.b)("div", {
						class: "inspectStudio"
					}, this.state.project ? this.renderMain() : this.state.loading ? this.renderLoading() : this.renderLoader())
				}
			}
		},
		96: function(e, t, o) {
			"use strict";
			o.r(t);
			var r = o(8);
			o.d(t, "lang", (function() {
				return r.c
			})), o.d(t, "setCurLang", (function() {
				return r.d
			})), o.d(t, "getLangs", (function() {
				return r.b
			})), o.d(t, "getCurLang", (function() {
				return r.a
			}))
		}
	}
]);