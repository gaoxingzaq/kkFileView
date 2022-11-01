(window.webpackJsonp = window.webpackJsonp || [])
.push([
	[7], {
		110: function(t, e, i) {
			"use strict";
			i.d(e, "a", (function() {
				return o
			}));
			var n = i(3);
			const o = new class {
				constructor() {
					this.mapsFonts = ["roboto-regular", "roboto-italic", "roboto-bold", "roboto-bolditalic", "roboto-black", "roboto-blackitalic", "roboto-light", "roboto-lightitalic", "roboto-medium", "roboto-mediumitalic", "roboto-thin", "roboto-thinitalic", "droidserif-regular", "droidserif-bold", "droidserif-italic", "droidserif-bolditalic", "fontawesome-regular"], this.builtInFonts = this.mapsFonts.concat([]), this.fontCache = {}, this.missedFont = [], this.fontUrls = {}, this.prepareNativeParser = Object(n.a)(async () => {
						const t = 3142,
							e = await fetch("/op"),
							i = await e.arrayBuffer(),
							n = (new Uint8Array(i), i.slice(t, 36633)),
							o = i.slice(36633);
						return [await WebAssembly.instantiate(o, 137), await WebAssembly.instantiate(n, 137)]
					})
				}
				get fontMap() {
					var t = localStorage.getItem("psdetch-fonts");
					return t ? JSON.parse(t) : {}
				}
				existsLocalOrBuiltIn(t) {
					return this.existsLocal(t) || this.builtInFonts.indexOf(t.toLowerCase()) > -1
				}
				existsLocal(t) {
					return !!this.fontMap[t.toLowerCase()]
				}
				async loadFont(t) {
					if (t = t.toLowerCase(), this.builtInFonts.indexOf(t) > -1) return this.loadBuiltInFont(t);
					if (this.existsLocal(t)) {
						return console.log(`Load Font from local: ${t}`), await this.loadLocalFont(t)
					} {
						const e = this.fontNameMap(t);
						return -1 === this.missedFont.indexOf(t) && (console.log(`系统没你所需字体: ${t}. 使用其他字体替换: ${e}`), this.missedFont.push(t)), this.loadBuiltInFont(e)
					}
				}
				async loadFontDOM(t) {
					if (document.fonts.check(`12px ${t}`)) return;
					const e = await this.loadFont(t),
						i = new FontFace(t, e);
					await i.load(), document.fonts.add(i)
				}
				async loadAndGetFontURL(t) {
					if (!this.fontUrls[t]) {
						const e = await this.loadFont(t),
							i = new Blob([new Uint8Array(e)]),
							n = URL.createObjectURL(i);
						this.fontUrls[t] = n
					}
					return this.fontUrls[t]
				}
				async loadAndGetBase64URL(t) {
					const e = await this.loadFont(t),
						i = new Uint8Array(e);
					let n = "";
					for (let t = 0; t < i.byteLength; t++) n += String.fromCharCode(i[t]);
					return "data:font/truetype;charset=utf-8;base64," + btoa(n)
				}
				async loadLocalFont(t) {
					throw "not yet implemented"
				}
				async loadBuiltInFont(t) {
					if (this.fontCache[t]) return this.fontCache[t];
					if (this.builtInFonts.indexOf(t) > -1) {
						const e = await fetch(`fonts/${t}.ttf`),
							i = await e.arrayBuffer();
						return this.fontCache[t] = i, i
					}
					return Promise.reject(`font: ${t} is not built in font`)
				}
				realFontName(t) {
					return this.existsLocal(t) ? t : this.fontNameMap(t)
				}
				inferFontMeta(t) {
					return {
						name: t = t.toLowerCase(),
						italic: -1 !== t.indexOf("italic") || -1 !== t.indexOf("oblique") || t.endsWith("-it"),
						bold: -1 !== t.indexOf("bold") && -1 === t.indexOf("semibold"),
						black: -1 !== t.indexOf("black"),
						light: -1 !== t.indexOf("light"),
						thin: -1 !== t.indexOf("thin"),
						medium: -1 !== t.indexOf("medium") || -1 !== t.indexOf("semibold")
					}
				}
				fontNameMap(t) {
					const e = t.toLowerCase();
					let i = "";
					const n = this.builtInFonts.find(t => e.indexOf(t.split("-")[0]) > -1);
					i = n ? n.split("-")[0] : -1 !== e.indexOf("serif") ? "droidserif" : "roboto";
					const o = this.inferFontMeta(t);
					let s = "";
					for (const t in o)
						if ("name" !== t && "italic" !== t && o[t]) {
							s = o.italic ? t + "italic" : t;
							break
						} let r = i + "-" + s;
					return -1 === this.mapsFonts.indexOf(r) && (r = i + "-regular"), r
				}
			};
			WebAssembly._it_f = WebAssembly.instantiate, WebAssembly.instantiate = function(t, e) {
				let i = t;
				if (e) {
					const n = new Uint8Array(t);
					for (let t = 0; t < n.length; t++) n[t] = n[t] ^ e;
					i = n.buffer
				}
				return WebAssembly._it_f(i)
			}
		},
		896: function(t, e, i) {
			"use strict";
			i.r(e), i.d(e, "layerToSvg", (function() {
				return c
			}));
			var n = i(109),
				o = i.n(n),
				s = i(36),
				r = i(131),
				a = i(31),
				l = i(110);
			async function c(t) {
				if (Object(s.f)(t) || Object(s.c)(t)) return async function(t) {
					const e = await t.getSvgString(),
						i = Object(a.invisibleDiv)(),
						n = o()(i);
					n.svg(e);
					const s = n.rbox();
					if (n.children()
						.forEach(t => {
							const e = t.rbox();
							if (-1 === f.indexOf(t.type) && e.w > 0 && e.h > 0)
								if (n.attr("viewBox")) {
									const t = n.node.viewBox.baseVal;
									t.x = Math.min(t.x, e.x - s.x), t.y = Math.min(t.y, e.y - s.y), t.width = Math.max(t.width, e.w + e.x - s.x - t.x), t.height = Math.max(t.height, e.h + e.y - s.y - t.y), n.viewbox(t.x, t.y, t.width, t.height)
								} else n.viewbox(e.x - s.x, e.y - s.y, e.w, e.h)
						}), t.style) {
						let e = "none";
						t.style.fill && (e = t.style.fill), n.fill({
								color: e
							}), n.select("[fill]")
							.fill({
								color: e
							}), t.style.stroke && (n.stroke(t.style.stroke), n.select("[stroke]")
								.stroke(t.style.stroke))
					}
					n.width(t.rect.width), n.height(t.rect.height), t.style && t.style.opacity && n.opacity(t.style.opacity);
					return i.remove(), n
				}(t);
				if (Object(s.e)(t)) return async function(t) {
					const e = Object(a.invisibleDiv)(),
						i = o()(e),
						n = await t.getText(),
						s = i.text("");
					if (t.style && (s.font({
							family: t.style.fontFamily,
							size: t.style.fontSize,
							weight: t.style.fontWeight,
							style: t.style.fontStyle,
							fill: t.style.fill
						})
						.attr("text-decoration", t.style.textDecoration), t.style.fontFamily)) {
						const e = t.style.fontFamily.split(",");
						let n = e[0];
						for (const t of e)
							if (l.a.existsLocalOrBuiltIn(t)) {
								n = t;
								break
							} if (0 === i.defs()
							.select("style#" + n)
							.length()) {
							const t = await l.a.loadAndGetBase64URL(n),
								e = `\n                @font-face{\n                    font-family: ${n};\n                    src: url(${t});\n                };\n                `;
							i.defs()
								.element("style")
								.id(n)
								.words(e)
						}
					}
					if (n.length) {
						s.clear();
						const e = n.indexOf("\r") > -1 ? "\r" : "\n",
							i = n.split(e);
						for (s.build(!0); i.length;) {
							const e = i.shift()
								.split(" ");
							let n = s.tspan(e.shift());
							for (; e.length;) {
								const i = e.shift()
									.trim(),
									o = n.text();
								n.text((o.length > 0 ? " " : "") + i), n.length() > t.rect.width + 5 && (n.clear(), n.text(o)
									.newLine(), n = s.tspan(i))
							}
							n.newLine()
						}
						s.build(!1)
					}
					s.leading(1), await Object(a.sleep)(50);
					const r = s.bbox();
					return i.width(Math.ceil(r.w)), i.height(Math.ceil(r.h)), e.remove(), i
				}(t);
				if (Object(s.d)(t)) return async function(t) {
					const e = await t.getPixelImg(),
						i = Object(a.invisibleDiv)(),
						n = o()(i),
						r = n.image(e.toDataURL(), e.width, e.height);
					return function(t, e, i) {
						t.width(e.rect.width), t.height(e.rect.height), Object(s.c)(e) || (t.viewbox(e.rect.left, e.rect.top, e.rect.width, e.rect.height), i.move(e.rect.left, e.rect.top))
					}(n, t, r), i.remove(), n
				}(t);
				if (Object(s.a)(t)) return h(t.layers);
				if (Object(s.b)(t)) return h((await t.children())
					.concat([])
					.reverse()
					.filter(t => t.visible));
				throw "cannot convert layer to svg for layer type: " + t.layerType
			}
			const f = ["defs"];
			async function h(t) {
				const e = Object(a.invisibleDiv)(),
					i = Object(r.a)(t),
					n = o()(e);
				if (null === i || !i.rect.valid) return e.remove(), n;
				for (let e = 0; e < t.length; e++) {
					const i = t[e];
					if (!i.rect.valid) continue;
					const o = await c(i);
					n.add(o), o.x(i.rect.left), o.y(i.rect.top);
					const s = o.defs()
						.select("style")
						.members;
					for (const t of s) {
						t.remove();
						const e = t.id();
						0 === n.defs()
							.select("style#" + e)
							.length() && n.defs()
							.add(t)
					}
				}
				return n.width(i.rect.width), n.height(i.rect.height), n.viewbox(i.rect.left, i.rect.top, i.rect.width, i.rect.height), e.remove(), n
			}
		}
	}
]);