<style>
area {
	--color: white;
	display: inline-block;
	height: 1em;
	width: 1em;
	vertical-align: baseline;
	border: 1px solid white;
	background-color: var(--color);
}

area[primary]
	{ --color: #5f0f40; }
area[secondary]
	{ --color: #efc88b; }
area[tertiary]
	{ --color: #fad2e1; }
area[text]
	{ --color: #121212; }
area[text-alt]
	{ --color: #1d2f6f; }
area[background]
	{ --color: #f8f8f8; }
</style>

# Colors
<area primary> Primary

<area secondary> Secondary

<area tertiary> Tertiary

<area text> Text

<area text-alt> Alternative Text

<area background> Background

# Reference Material
All guidelines from the [Material Design Guidelines](https://m3.material.io/) should be followed unless superceded by a rule outlined in this document.

# Constraints
## Padding and Margins
- Padding and margins should never be an odd number.
- Aim for multiples of 16, 12, then 6, then 4, then 2. That is, if a padding can have a value that is a multiple of 12, that should be the top priority. If a multiple of 12 is too imprecise, reduce to a multiple of 6, and so forth.
- Default padding on container items should `16px` vertical `24px` horizontal. That is, `padding: 16px 24px;`

## Corners
Prefer sharp corners where possible. Rounded corners should be the exception, whereas sharp corners are the rule.

## Interactivity
All clickable elements must have, at the very least, `cursor: pointer`. If a different cursor is more descriptive, that should be used instead.

All clickable elements should either appear as a link (that is, underlined and link colors) or be clear buttons. For button-style elements, click ripples should be used whenever possible.

Additionally ensure a reasonable tab order on the page. All focusable elements *must* have a unified focus style.

## Desktop First, Mobile Required
The app should be developed desktop first. This means that the interface should first and foremost be appealing on desktop. Despite this, mobile should not be neglected. Once assured that the page looks appealing on desktop, the mobile view should be made as appealing as possible without impeding the desktop appearance.

## Color Usage
Color usage should follow a similar pattern to [Ålesund Kommune's website](https://alesund.kommune.no/) and their [subsites](https://alesund.kommune.no/kultur-idrett-og-fritid/).

This includes:
- Important sections of the page should be highlighted with a background color of either the <area primary> primary, <area secondary> secondary, or <area tertiary> tertiary colors.
- Important headlines that are not contained by a section should use the <area text-alt> alternative text color.
- Most common, content-rich buttons (having at least text) that are not within a container should use the <area text-alt> alternative text color as its background.