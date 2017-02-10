var World = {
	tracker: null,
    cloudRecognitionService: null,

    loaded: false,
    rotating: false,
    trackableVisible: false,
    snapped: false,
    resourcesLoaded: false,
    interactionContainer: 'snapContainer',
    layout: {
    	normal: {
    		offsetX: 0.35,
    		offsetY: 0.45,
    		opacity: 0.0,
    		carScale: 0.06,
    		carTranslateY: 0.05
    	},
    	snapped: {
    		offsetX: 0.45,
    		offsetY: 0.45,
    		opacity: 0.2,
    		carScale: 0.06,
    		carTranslateY: 0
    	}
    },
    previousDragValue: { x: 0, y: 0 },
    previousScaleValue: 0,
    previousScaleValueButtons: 0,
    previousRotationValue: 0,
    previousTranslateValueRotate: { x: 0, y: 0 },
    previousTranslateValueSnap: { x: 0, y: 0 },
    defaultScale: 0,

	init: function initFn() {
//	    AR.context.setCloudRecognitionServerRegion(AR.CONST.CLOUD_RECOGNITION_SERVER_REGION.CHINA);
	    AR.context.setCloudRecognitionServerRegion(AR.CONST.CLOUD_RECOGNITION_SERVER_REGION.CHINA,{
            "serverUrl": "https://api-cn-broadmesse-client.wikitude.com/cloudrecognition",
            "onSuccess": function(msg) {console.info("all good"); },
            "onError": function(msg) {console.info("error occurred"); }
        });
		this.createTracker();
		this.createOverlays();
	},

	/*
		First an AR.ImageTracker connected with an AR.CloudRecognitionService needs to be created in order to start the recognition engine.
		It is initialized with your clinet token and the id of one of your target collections.
		Optional parameters are passed as object in the last argument. In this case callback functions for the onInitialized and onError triggers are set.
		Once the tracker is fully loaded the function trackerLoaded() is called, should there be an error initializing the CloudRecognitionService the
		function trackerError() is called instead.
	*/
	createTracker: function createTrackerFn() {
		/*World.cloudRecognitionService = new AR.CloudRecognitionService("1eaaa2bcb2a053c161f91af5d22f10ae", "5857a06443ed04964e1ae211", {
			onInitialized: this.trackerLoaded,
			onError: this.trackerError
		});*/

        //宽创
		World.cloudRecognitionService = new AR.CloudRecognitionService("6a92be07ae8ea695992ab6d38a578b32", "589bfabf461792f27d3bcb99", {
        	onInitialized: this.trackerLoaded,
        	onError: this.trackerError
        });

		World.tracker = new AR.ImageTracker(this.cloudRecognitionService, {
			onError: this.trackerError
		});
	},

	startContinuousRecognition: function startContinuousRecognitionFn(interval) {
		/*
			With this function call the continuous recognition mode is started. It is passed three parameters, the first defines the interval in which
			a new recognition is started. It is set in milliseconds and the minimum value is 500. The second parameter defines a callback function for
			when a recognition cycle is completed. The third and last paramater defines another callback function. This callback is called by the server
			if the recognition interval was set too high for the current network speed.
		*/
		this.cloudRecognitionService.startContinuousRecognition(interval, this.onInterruption, this.onRecognition, this.onRecognitionError);
	},

	/*
		Callback function to handle CloudRecognitionService initialization errors.
	*/
	trackerError: function trackerErrorFn(errorMessage) {
		alert(errorMessage);
	},

	createOverlays: function createOverlaysFn() {

	},

	/*
		In this function the continuous recognition will be started, after the tracker finished loading.
	*/
	onRecognition: function onRecognitionFn(recognized, response) {
		if (recognized) {
		    World.resourcesLoaded=true;

            if (World.model3D !== undefined) {
            	World.model3D.destroy();
            }

            World.model3D = new AR.Model(response.metadata.D_url, {
                onLoaded: World.loadingStep,
                onClick: World.toggleAnimateModel,
                scale: {
                    x: response.metadata.scale.x,
                    y: response.metadata.scale.y,
                    z: response.metadata.scale.z
                },
                translate: {
                    x: 0.0,
                    y: 0.05,
                    z: 0.0
                },
                rotate: {
                    z: 335
                },
                onScaleBegan: World.onScaleBegan,
                onScaleChanged: World.onScaleChanged,
                onScaleEnded: function(scale) {
                },
                onDragBegan: function(x, y) {
                },
                onDragChanged: function(x, y) {
                    if (World.snapped) {
                        var movement = { x:0, y:0 };


                        /* Calculate the touch movement between this event and the last one */
                        movement.x = World.previousDragValue.x - x;
                        movement.y = World.previousDragValue.y - y;

                        /* Rotate the car model accordingly to the calculated movement values and the current orientation of the model. */
                        this.rotate.y += (Math.cos(this.rotate.z * Math.PI / 180) * movement.x * -1 + Math.sin(this.rotate.z * Math.PI / 180) * movement.y) * 180;
                        this.rotate.x += (Math.cos(this.rotate.z * Math.PI / 180) * movement.y + Math.sin(this.rotate.z * Math.PI / 180) * movement.x) * -180;

                        World.previousDragValue.x = x;
                        World.previousDragValue.y = y;
                    }
                },
                onDragEnded: function(x, y) {
                    if (World.snapped) {
                        World.previousDragValue.x = 0;
                        World.previousDragValue.y = 0;
                    }
                },
                onRotationBegan: function(angleInDegrees) {
                },
                onRotationChanged: function(angleInDegrees) {
                   this.rotate.z = previousRotationValue - angleInDegrees;
                },
                onRotationEnded: function(angleInDegrees) {
                   previousRotationValue = this.rotate.z
                }
            });

            //---------------------------------
//            World.appearingAnimation = World.createAppearingAnimation(World.model3D, 0.12);
            World.appearingAnimation = World.createAppearingAnimation(World.model3D, response.metadata.modelscale);
            World.rotationAnimation = new AR.PropertyAnimation(World.model3D, "rotate.roll", -25, 335, 10000);
            //---------------------------

            if (World.buttonRotate !== undefined) {
            	World.buttonRotate.destroy();
            }

            var imgRotate = new AR.ImageResource("assets/rotateButton.png");
            World.buttonRotate = new AR.ImageDrawable(imgRotate, 0.14, {
            	offsetX: 0.20,
            	offsetY: 0.30,
            	onClick: World.toggleAnimateModel
            });

            if (World.buttonSnap !== undefined) {
            	World.buttonSnap.destroy();
            }

            var imgSnap = new AR.ImageResource("assets/snapButton.png");
            World.buttonSnap = new AR.ImageDrawable(imgSnap, 0.14, {
                offsetX: -0.20,
                offsetY: -0.30,
                onClick: World.toggleSnapping
            });

			if (World.modelArguments !== undefined) {
				World.modelArguments.destroy();
			}

			/*
				The following combines everything by creating an AR.ImageTrackable using the CloudRecognitionService, the name of the image target and
				the drawables that should augment the recognized image.
			*/	
			World.modelArguments = new AR.ImageTrackable(World.tracker, response.targetInfo.name , {
				drawables: {
					cam: [World.model3D,World.buttonSnap,World.buttonRotate]
				},
                snapToScreen: {
                	snapContainer: document.getElementById('snapContainer')
                },
                onEnterFieldOfVision: World.appear,
                onExitFieldOfVision: World.disappear
			});

//			World.toggleAnimateModel();

			//---------------------------

		}else{
//		    alert("failed");
		}
	},

	loadingStep: function loadingStepFn() {
		if (!World.loaded && World.resourcesLoaded && World.model3D.isLoaded()) {
			World.loaded = true;
			if ( World.trackableVisible && !World.appearingAnimation.isRunning() ) {
			    World.toggleSnapping();
				World.appearingAnimation.start();
			}

			var cssDivLeft = " style='display: table-cell;vertical-align: middle; text-align: right; width: 50%; padding-right: 15px;'";
			var cssDivRight = " style='display: table-cell;vertical-align: middle; text-align: left;'";
			document.getElementById('loadingMessage').innerHTML =
				"<div" + cssDivLeft + ">Scan CarAd ClientTracker Image:</div>" +
				"<div" + cssDivRight + "><img src='assets/car.png'></img></div>";

			// Remove Scan target message after 10 sec.
			setTimeout(function() {
				var e = document.getElementById('loadingMessage');
				e.parentElement.removeChild(e);
			}, 10000);
		}
    },

    createAppearingAnimation: function createAppearingAnimationFn(model, scale) {
		/*
			The animation scales up the 3D model once the target is inside the field of vision. Creating an animation on a single property of an object is done using an AR.PropertyAnimation. Since the car model needs to be scaled up on all three axis, three animations are needed. These animations are grouped together utilizing an AR.AnimationGroup that allows them to play them in parallel.

			Each AR.PropertyAnimation targets one of the three axis and scales the model from 0 to the value passed in the scale variable. An easing curve is used to create a more dynamic effect of the animation.
		*/
		var sx = new AR.PropertyAnimation(model, "scale.x", 0, scale, 1500, {
			type: AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC
		});
		var sy = new AR.PropertyAnimation(model, "scale.y", 0, scale, 1500, {
			type: AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC
		});
		var sz = new AR.PropertyAnimation(model, "scale.z", 0, scale, 1500, {
			type: AR.CONST.EASING_CURVE_TYPE.EASE_OUT_ELASTIC
		});
//		,{onFinish: World.toggleSnapping}
		return new AR.AnimationGroup(AR.CONST.ANIMATION_GROUP_TYPE.PARALLEL, [sx, sy, sz]);
	},

	appear: function appearFn() {
    	World.trackableVisible = true;
    	if ( World.loaded && !World.snapped ) {
    		// Resets the properties to the initial values.
    		World.resetModel();
    		World.toggleSnapping();
    		World.appearingAnimation.start();

    	}
    },
    disappear: function disappearFn() {
    	World.trackableVisible = false;
    },

    resetModel: function resetModelFn() {
    	World.rotationAnimation.stop();
    	World.rotating = false;
    	World.model3D.rotate.x = 0;
    	World.model3D.rotate.y = 0;
    	World.model3D.rotate.z = 335;
    },

	toggleSnapping: function toggleSnappingFn() {
        /*AR.context.openInBrowser("https://www.baidu.com/");*/
		if (World.appearingAnimation.isRunning()) {
			World.appearingAnimation.stop();
		}
		World.snapped = !World.snapped;
		World.modelArguments.snapToScreen.enabled = World.snapped;
		if (World.snapped) {
			World.applyLayout(World.layout.snapped);
		} else {
			World.applyLayout(World.layout.normal);
		}
    },

	applyLayout: function applyLayoutFn(layout) {

		World.buttonRotate.translate.x = layout.offsetX;
		World.buttonRotate.translate.y = layout.offsetY;

		World.buttonSnap.translate.x = -layout.offsetX;
		World.buttonSnap.translate.y = -layout.offsetY;

		World.buttonRotate.scale.x =  1;
		World.buttonRotate.scale.y =  1;
		World.buttonSnap.scale.x =  1;
		World.buttonSnap.scale.y =  1;

		World.model3D.scale = {
			x: layout.carScale,
			y: layout.carScale,
			z: layout.carScale
		};

		World.defaultScale = layout.carScale;

		World.model3D.translate = {
			x: 0.0,
			y: layout.carTranslateY,
			z: 0.0
		};
	},
	onScaleBegan: function(scale) {
		if (World.snapped) {
			World.previousScaleValue = World.model3D.scale.x;
			World.previousScaleValueButtons = World.buttonRotate.scale.x;

			World.previousTranslateValueRotate.x = World.buttonRotate.translate.x;
			World.previousTranslateValueRotate.y = World.buttonRotate.translate.x;

			World.previousTranslateValueSnap.x = World.buttonSnap.translate.x;
			World.previousTranslateValueSnap.y = World.buttonSnap.translate.x;
		}
    },
    onScaleChanged: function(scale) {
    	if (World.snapped) {
           World.model3D.scale.x = World.previousScaleValue * scale;
           World.model3D.scale.y = World.model3D.scale.x;
           World.model3D.scale.z = World.model3D.scale.x;

           World.buttonRotate.scale.x =  World.previousScaleValueButtons * scale;
           World.buttonRotate.scale.y =  World.buttonRotate.scale.x;

           World.buttonSnap.scale.x =  World.buttonRotate.scale.x;
           World.buttonSnap.scale.y =  World.buttonRotate.scale.x;

           World.buttonRotate.translate.x = World.previousTranslateValueRotate.x * scale;
           World.buttonRotate.translate.y = World.previousTranslateValueRotate.y * scale;

           World.buttonSnap.translate.x = World.previousTranslateValueSnap.x * scale;
           World.buttonSnap.translate.y = World.previousTranslateValueSnap.y * scale;
   		}
    },

    toggleAnimateModel: function toggleAnimateModelFn() {
    	if (!World.rotationAnimation.isRunning()) {
    		if (!World.rotating) {
    			// Starting an animation with .start(-1) will loop it indefinitely.
    			World.rotationAnimation.start(-1);
    			World.rotating = true;
    		} else {
    			// Resumes the rotation animation
    			World.rotationAnimation.resume();
    		}
    	} else {
    		// Pauses the rotation animation
    		World.rotationAnimation.pause();
    	}
    	return false;
    },

    //----------------------------------------------

	onRecognitionError: function onRecognitionError(errorCode, errorMessage) {
		alert("error code: " + errorCode + " error message: " + JSON.stringify(errorMessage));
	},

	/*
		In case the current network the user is connected to, isn't fast enough for the set interval. The server calls this callback function with
		a new suggested interval. To set the new interval the recognition mode first will be restarted.
	*/
	onInterruption: function onInterruptionFn(suggestedInterval) {
		World.cloudRecognitionService.stopContinuousRecognition();
		World.cloudRecognitionService.startContinuousRecognition(suggestedInterval);
	},

	trackerLoaded: function trackerLoadedFn() {
		World.startContinuousRecognition(2000);
//		World.showUserInstructions();
//		World.resourcesLoaded=true;
	},

	showUserInstructions: function showUserInstructionsFn() {
		var cssDivLeft = " style='display: table-cell;vertical-align: middle; text-align: right; width: 20%; padding-right: 15px;'";
		var cssDivRight = " style='display: table-cell;vertical-align: middle; text-align: center;'";
		var img = "style='margin-right:5px'";

		document.getElementById('messageBox').innerHTML =
			"<div" + cssDivLeft + ">Scan: </div>" +
			"<div" + cssDivRight + ">" +
				"<img " + img + " src='assets/austria.jpg'></img>" +
				"<img " + img + " src='assets/brazil.jpg'></img>" +
				"<img " + img + " src='assets/france.jpg'></img>" +
				"<img " + img + " src='assets/germany.jpg'></img>" +
				"<img " + img + " src='assets/italy.jpg'></img>" +
			"</div>";

		setTimeout(function() {
			var e = document.getElementById('messageBox');
			e.parentElement.removeChild(e);
		}, 10000);			
	}

};

World.init();
