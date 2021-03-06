var World = {
	tracker: null,
    cloudRecognitionService: null,

//    hasVideoStarted:false,

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
    		carTranslateY: 0
    	},
    	snapped: {
    		offsetX: 0.35,
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
		World.cloudRecognitionService = new AR.CloudRecognitionService("6a92be07ae8ea695992ab6d38a578b32", "58a166af461792f27d3bcba8", {
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
//            alert("su");

            if(World.video !==undefined){
                World.video.pause();
            }

		    if(response.metadata.type===2){

                var div = document.getElementById("snapContainer");
                div.className = "";

		        World.resourcesLoaded=true;

                if (World.model3D !== undefined) {
                	World.model3D.destroy();
                }

                World.model3D = new AR.Model(response.metadata.D_url, {
                    onLoaded: World.loadingStep,
//                    onClick: World.toggleAnimateModel,
                    onClick: function(){
                        var wvUrl2 = "architectsdk://link?uri="+ encodeURIComponent(response.metadata.target_url)+"&title="+ encodeURIComponent(response.metadata.title)+"&content="+ encodeURIComponent(response.metadata.content);
                        document.location = wvUrl2;
                    },

                    scale: {
                        x: response.metadata.scale.x,
                        y: response.metadata.scale.y,
                        z: response.metadata.scale.z
                    },
                    translate: {
                    	x: response.metadata.translate.x,
                    	y: response.metadata.translate.y,
                    	z: response.metadata.translate.z
                    },
                    rotate: {
                        y: response.metadata.rotate.yaw,
                    	z: response.metadata.rotate.roll,
                    	x: response.metadata.rotate.tilt
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

                            movement.x = World.previousDragValue.x - x;
                            movement.y = World.previousDragValue.y - y;

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
                World.appearingAnimation = World.createAppearingAnimation(World.model3D, response.metadata.model_scale);
                World.rotationAnimation = new AR.PropertyAnimation(World.model3D, "rotate.roll", -25, 335, 10000);
                //---------------------------

                if (World.imgSnap !== undefined) {
                	World.imgSnap.destroy();
                }

                if (World.buttonSnap !== undefined) {
                	World.buttonSnap.destroy();
                }

                World.imgSnap = new AR.ImageResource("assets/snapButton.png");
                World.buttonSnap = new AR.ImageDrawable(World.imgSnap, 0.2, {
                    offsetX: -0.20,
                    offsetY: -0.30,
                    onClick: World.toggleSnapping
                });
                //-----------------------------

                if (World.modelArguments !== undefined) {
                	World.modelArguments.destroy();
                }

                World.modelArguments = new AR.ImageTrackable(World.tracker, response.targetInfo.name , {
                	drawables: {
                		cam: [World.model3D,World.buttonSnap]
                	},
                    snapToScreen: {
                    	snapContainer: document.getElementById('snapContainer')
                    },
                    onEnterFieldOfVision: function onEnterFieldOfVisionFn () {
                        World.appear();
                    },
                    onExitFieldOfVision: function onExitFieldOfVisionFn() {
                        World.disappear();
                    }
                });

		    }else if(response.metadata.type===3){

//		        var div = document.getElementById("snapContainer");
//                div.className = "selected";

		        // Create play button which is used for starting the video
                var playButtonImg = new AR.ImageResource("assets/playButton.png");
                var playButton = new AR.ImageDrawable(playButtonImg, 0.3, {
                    enabled: false,
                    clicked: false,
                    zOrder: 2,
                    onClick: function playButtonClicked() {
                        World.video.play(1);
                        World.video.playing = true;
                        playButton.clicked = true;
                    },
                    translate: {
                        y: -0.3
                    }
                });

                if(World.video!==undefined){
                    World.video.destroy();
                }

                World.video = new AR.VideoDrawable(response.metadata.video_url, 0.40, {
                	translate: { y:playButton.translate.y },
                	zOrder: 1,
                	onLoaded: function videoLoaded() {
                		playButton.enabled = true;
                	},
                	onPlaybackStarted: function videoPlaying() {
                		playButton.enabled = false;
                		World.video.enabled = true;
                	},
                	onFinishedPlaying: function videoFinished() {
                		playButton.enabled = true;
                		World.video.playing = false;
                		World.video.enabled = false;
                	},
                	onClick: function videoClicked() {
                		if (playButton.clicked) {
                			playButton.clicked = false;
                		} else if (World.video.playing) {
                			World.video.pause();
                			World.video.playing = false;
                		} else {
                			World.video.resume();
                			World.video.playing = true;
                		}
                	}
                });

                if (World.modelArguments !== undefined) {
                	World.modelArguments.destroy();
                }

                World.modelArguments = new AR.ImageTrackable(World.tracker, "*" , {
                	drawables: {
                    	cam: [World.video, playButton]
                    },
                    onEnterFieldOfVision: function onEnterFieldOfVisionFn() {
                    	World.modelArguments.snapToScreen.enabled = false;
                    },
                    onExitFieldOfVision: function onExitFieldOfVisionFn() {

                    },
                    snapToScreen: {
                    	enabledOnExitFieldOfVision: true,
                    	snapContainer: document.getElementById('snapContainer')
                    }
                });
		    }else if(response.metadata.type===1){

                var div = document.getElementById("snapContainer");
                div.className = "selected";

                if (World.imgButton2 !== undefined) {
                	World.imgButton2.destroy();
                }

                if (World.buttonOverlay2 !== undefined) {
                	World.buttonOverlay2.destroy();
                }

                World.imgButton2 = new AR.ImageResource(response.metadata.picture_url);
                World.buttonOverlay2 = new AR.ImageDrawable(World.imgButton2, 0.15, {
                	translate: {
                		x: 0,
                		y: 0.15
                	}
                });

                World.buttonOverlay2.onClick = function() {
                     var wvUrl2 = "architectsdk://link?uri="+ encodeURIComponent(response.metadata.target_url)+"&title="+ encodeURIComponent(response.metadata.title)+"&content="+ encodeURIComponent(response.metadata.content);
                     document.location = wvUrl2;
                };

                if (World.modelArguments !== undefined) {
                	World.modelArguments.destroy();
                }

                World.modelArguments = new AR.ImageTrackable(World.tracker, response.targetInfo.name , {
                	drawables: {
                		cam: [World.buttonOverlay2]
                	}
                });
		    }
            //You must set it here
		    World.snapped=false;


		}else{
//		    alert("failed");
		}
	},

	loadingStep: function loadingStepFn() {
		if (!World.loaded && World.resourcesLoaded && World.model3D.isLoaded()) {
			World.loaded = true;
			if ( World.trackableVisible && !World.appearingAnimation.isRunning() ) {
//			    World.toggleSnapping();
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
//    		World.toggleSnapping();
    		World.appearingAnimation.start();

    	}
    },
    disappear: function disappearFn() {
    	World.trackableVisible = false;
    },

    resetModel: function resetModelFn() {
    	World.rotationAnimation.stop();
    	World.rotating = false;
    },

	toggleSnapping: function toggleSnappingFn() {
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

		World.buttonSnap.translate.x = -layout.offsetX;
		World.buttonSnap.translate.y = -layout.offsetY;

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
		}
    },
    onScaleChanged: function(scale) {
    	if (World.snapped) {
           World.model3D.scale.x = World.previousScaleValue * scale;
           World.model3D.scale.y = World.model3D.scale.x;
           World.model3D.scale.z = World.model3D.scale.x;
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

	onRecognitionError: function onRecognitionError(errorCode, errorMessage) {
		alert("error code: " + errorCode + " error message: " + JSON.stringify(errorMessage));
	},

	onInterruption: function onInterruptionFn(suggestedInterval) {
		World.cloudRecognitionService.stopContinuousRecognition();
		World.cloudRecognitionService.startContinuousRecognition(suggestedInterval);
	},

	trackerLoaded: function trackerLoadedFn() {
		World.startContinuousRecognition(2000);
	}

};

World.init();
