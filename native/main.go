package main

// Copyright (c) 2018 Bhojpur Consulting Private Limited, India. All rights reserved.

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

import (
	"fmt"
	"log"
	"net/url"

	gui "github.com/bhojpur/gui/pkg/engine"
	"github.com/bhojpur/gui/pkg/engine/app"
	"github.com/bhojpur/gui/pkg/engine/container"
	"github.com/bhojpur/gui/pkg/engine/layout"
	"github.com/bhojpur/gui/pkg/engine/theme"
	"github.com/bhojpur/gui/pkg/engine/widget"
)

var topWindow gui.Window

func main() {
	// initilize the Bhojpur GUI application
	ode := app.NewWithID("net.bhojpur.ode")
	ode.SetIcon(theme.BhojpurLogo())
	wm := ode.NewWindow("Bhojpur ODE")
	topWindow = wm

	wm.SetMainMenu(makeMenu(ode, wm))
	wm.SetMaster()
	content := container.NewMax()
	title := widget.NewLabel("Component name")
	intro := widget.NewLabel("An introduction would probably go\nhere, as well as a")
	intro.Wrapping = gui.TextWrapWord
	tutorial := container.NewBorder(
		container.NewVBox(title, widget.NewSeparator(), intro), nil, nil, nil, content)
	wm.SetContent(tutorial)

	wm.Resize(gui.NewSize(640, 460))
	wm.ShowAndRun()
}

func logLifecycle(a gui.App) {
	a.Lifecycle().SetOnStarted(func() {
		log.Println("Lifecycle: Started")
	})
	a.Lifecycle().SetOnStopped(func() {
		log.Println("Lifecycle: Stopped")
	})
	a.Lifecycle().SetOnEnteredForeground(func() {
		log.Println("Lifecycle: Entered Foreground")
	})
	a.Lifecycle().SetOnExitedForeground(func() {
		log.Println("Lifecycle: Exited Foreground")
	})
}

func makeMenu(a gui.App, w gui.Window) *gui.MainMenu {
	fileNewFile := gui.NewMenuItem("New File", nil)
	fileSaveAll := gui.NewMenuItem("Save All", nil)
	fileSaveAll.Disabled = true

	otherItem := gui.NewMenuItem("Other", nil)
	otherItem.ChildMenu = gui.NewMenu("",
		gui.NewMenuItem("Project", func() {
			fmt.Println("Menu New->Other->Project")
		}),
		gui.NewMenuItem("Mail", func() {
			fmt.Println("Menu New->Other->Mail")
		}),
	)
	fileNewFile.ChildMenu = gui.NewMenu("",
		gui.NewMenuItem("File", func() {
			fmt.Println("Menu New->File")
		}),
		gui.NewMenuItem("Directory", func() {
			fmt.Println("Menu New->Directory")
		}),
		otherItem,
	)
	settingsItem := gui.NewMenuItem("Settings", func() {
		w := a.NewWindow("Application Settings")
		//w.SetContent(settings.NewSettings().LoadAppearanceScreen(w))
		w.Resize(gui.NewSize(480, 480))
		w.Show()
	})

	// a Quit item will be appended to our first (File) menu
	menuFile := gui.NewMenu("File",
		fileNewFile,
		fileSaveAll,
	)
	if !gui.CurrentDevice().IsMobile() {
		menuFile.Items = append(menuFile.Items,
			gui.NewMenuItemSeparator(),
			settingsItem,
		)
	}

	cutItem := gui.NewMenuItem("Cut", func() {
		shortcutFocused(&gui.ShortcutCut{
			Clipboard: w.Clipboard(),
		}, w)
	})
	copyItem := gui.NewMenuItem("Copy", func() {
		shortcutFocused(&gui.ShortcutCopy{
			Clipboard: w.Clipboard(),
		}, w)
	})
	pasteItem := gui.NewMenuItem("Paste", func() {
		shortcutFocused(&gui.ShortcutPaste{
			Clipboard: w.Clipboard(),
		}, w)
	})
	findItem := gui.NewMenuItem("Find", func() {
		fmt.Println("Menu Find")
	})
	menuEdit := gui.NewMenu("Edit",
		cutItem,
		copyItem,
		pasteItem,
		gui.NewMenuItemSeparator(),
		findItem,
	)

	menuSelect := gui.NewMenu("Select",
		cutItem,
		copyItem,
		pasteItem,
		gui.NewMenuItemSeparator(),
		findItem,
	)

	menuViewCommandPalette := gui.NewMenuItem("Command Palette", func() {
		shortcutFocused(&gui.ShortcutPaste{
			Clipboard: w.Clipboard(),
		}, w)
	})
	menuViewToolbar := gui.NewMenuItem("Show Toolbar", nil)
	menuViewToolbar.Checked = true
	menuView := gui.NewMenu("View",
		menuViewCommandPalette,
		gui.NewMenuItemSeparator(),
		menuViewToolbar,
	)

	menuImage := gui.NewMenu("Image",
		cutItem,
		copyItem,
		pasteItem,
		gui.NewMenuItemSeparator(),
		findItem,
	)

	menuHelp := gui.NewMenu("Help",
		gui.NewMenuItem("Product Documentation", func() {
			u, _ := url.Parse("https://docs.bhojpur.net")
			_ = a.OpenURL(u)
		}),
		gui.NewMenuItem("Customer Support", func() {
			u, _ := url.Parse("https://desk.bhojpur-consulting.com/")
			_ = a.OpenURL(u)
		}),
		gui.NewMenuItemSeparator(),
		gui.NewMenuItem("Bhojpur Consulting", func() {
			u, _ := url.Parse("https://www.bhojpur-consulting.com")
			_ = a.OpenURL(u)
		}))

	return gui.NewMainMenu(
		menuFile,
		menuEdit,
		menuSelect,
		menuView,
		menuImage,
		menuHelp,
	)
}

func makeNav(setTutorial func(), loadPrevious bool) gui.CanvasObject {
	a := gui.CurrentApp()

	themes := gui.NewContainerWithLayout(layout.NewGridLayout(2),
		widget.NewButton("Dark", func() {
			a.Settings().SetTheme(theme.DarkTheme())
		}),
		widget.NewButton("Light", func() {
			a.Settings().SetTheme(theme.LightTheme())
		}),
	)

	return container.NewBorder(nil, themes, nil, nil, nil)
}

func shortcutFocused(s gui.Shortcut, w gui.Window) {
	if focused, ok := w.Canvas().Focused().(gui.Shortcutable); ok {
		focused.TypedShortcut(s)
	}
}
